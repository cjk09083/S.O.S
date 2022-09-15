#include "wifi.h"
#include <apps/netutils/mqtt_api.h>
#include <apps/netutils/dhcpc.h>
#include <stdio.h>
#include <apps/shell/tash.h>

#include <tinyara/gpio.h>
#include <tinyara/config.h>
#include <tinyara/analog/adc.h>
#include <tinyara/analog/ioctl.h>
#include <time.h>
#include <string.h>
#include <stdlib.h>
#include <apps/netutils/cJSON.h>
// for NTP
#include <apps/netutils/ntpclient.h>
#include <apps/netutils/webclient.h>

#include <artik_module.h>
#include <artik_http.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>

#include <artik_loop.h>

#include <fcntl.h>
#include <tinyara/pwm.h>

#include "basic_function.h"


// http 관련
#define ARRAY_SIZE(a) (sizeof(a) / sizeof((a)[0]))



#define DEFAULT_CLIENT_ID "123456789"
// artik broker
#define SERVER_ADDR "api.artik.cloud"
//#define SERVER_ADDR "52.86.204.150"
// security mode
#define SERVER_PORT 8883
//#define SERVER_PORT 1883 // non-secure mode, Not supported in ARTIK Cloud

#define RED_ON_BOARD_LED 45
#define NET_DEVNAME "wl1"




#define GPIO_FUNC_SHIFT 13
#define GPIO_INPUT (0x0 << GPIO_FUNC_SHIFT)
#define GPIO_OUTPUT (0x1 << GPIO_FUNC_SHIFT)

#define GPIO_PORT_SHIFT 3
#define GPIO_PORTG3 (0x7 << GPIO_PORT_SHIFT)
#define GPIO_PORTG2 (0x6 << GPIO_PORT_SHIFT)
#define GPIO_PORTG1 (0x5 << GPIO_PORT_SHIFT)
#define GPIO_PORTG0 (0x4 << GPIO_PORT_SHIFT)

#define GPIO_PIN_SHIFT 0
#define GPIO_PIN0 (0x0 << GPIO_PIN_SHIFT)
#define GPIO_PIN1 (0x1 << GPIO_PIN_SHIFT)
#define GPIO_PIN2 (0x2 << GPIO_PIN_SHIFT)
#define GPIO_PIN3 (0x3 << GPIO_PIN_SHIFT)
#define GPIO_PIN4 (0x4 << GPIO_PIN_SHIFT)
#define GPIO_PIN5 (0x5 << GPIO_PIN_SHIFT)
#define GPIO_PIN6 (0x6 << GPIO_PIN_SHIFT)
#define GPIO_PIN7 (0x7 << GPIO_PIN_SHIFT)

#define GPIO_PUPD_SHIFT 11
#define GPIO_PULLDOWN (0x1 << GPIO_PUPD_SHIFT)
#define GPIO_PULLUP (0x3 << GPIO_PUPD_SHIFT)


// XGPIO1
#define	fire_signal			(GPIO_INPUT | GPIO_PORTG0 | GPIO_PIN1 | GPIO_PULLDOWN)
// XGPIO2
#define id_sw3				(GPIO_INPUT | GPIO_PORTG0 | GPIO_PIN2 | GPIO_PULLDOWN)
// XGPIO3
#define emergency_signal	(GPIO_INPUT | GPIO_PORTG0 | GPIO_PIN3 | GPIO_PULLDOWN)
// XGPIO4
#define id_sw1				(GPIO_INPUT | GPIO_PORTG0 | GPIO_PIN4 | GPIO_PULLDOWN)
// XGPIO5
#define id_sw2				(GPIO_INPUT | GPIO_PORTG0 | GPIO_PIN5 | GPIO_PULLDOWN)
// XGPIO6
#define gas_onoff			(GPIO_OUTPUT | GPIO_PORTG0 | GPIO_PIN6 | GPIO_PULLDOWN)
// XGPIO7
#define gas_sig				(GPIO_INPUT | GPIO_PORTG0 | GPIO_PIN7 | GPIO_PULLDOWN)
// XGPIO8
#define eq_int2				(GPIO_INPUT | GPIO_PORTG1 | GPIO_PIN0 | GPIO_PULLDOWN)
// XGPIO9
#define interphone_signal	(GPIO_INPUT | GPIO_PORTG1 | GPIO_PIN1 | GPIO_PULLDOWN)
// XGPIO10
#define door_signal			(GPIO_INPUT | GPIO_PORTG1 | GPIO_PIN2 | GPIO_PULLDOWN)
// XGPIO11
#define eq_int1				(GPIO_INPUT | GPIO_PORTG1 | GPIO_PIN3 | GPIO_PULLDOWN)
// XGPIO12
#define power_signal		(GPIO_INPUT | GPIO_PORTG1 | GPIO_PIN4 | GPIO_PULLDOWN)
// XGPIO23
#define motion_a_signal		(GPIO_INPUT | GPIO_PORTG2 | GPIO_PIN7)
// XGPIO24
#define motion_b_signal		(GPIO_INPUT | GPIO_PORTG3 | GPIO_PIN0 | GPIO_PULLDOWN)
// XGPIO25
#define motion_c_signal		(GPIO_INPUT | GPIO_PORTG3 | GPIO_PIN1 | GPIO_PULLDOWN)
// XGPIO26
#define motion_d_signal		(GPIO_INPUT | GPIO_PORTG3 | GPIO_PIN2 | GPIO_PULLDOWN)


void power_control(void);
int power_cnt = 0;
int power_flag = 1;
int power_condition = 1;

void door_control(void);
int door_cnt = 0;
int door_flag = 1;
int door_condition = 1;

void earthquake_control(void);
int earthquake_cnt = 0;
int earthquake_flag = 1;
int earthquake_condition = 1;
int earthquake_send = 0;

void emergency_control(void);
int emergency_cnt = 0;
int emergency_condition = 1;

void gas_control(void);
int gas_cnt = 0;
int gas_flag = 1;
int gas_condition = 1;

void motion_control(void);
int motion_a_situation = 0;
int motion_b_situation = 0;
int motion_c_situation = 0;
int motion_d_situation = 0;
int motion_id = 0;

void id_control(void);
int id_sw1_val = 0;
int id_sw2_val = 0;
int id_sw3_val = 0;
int id_val = 0;

void interphone_control(void);
int interphone_flag = 0;
int interphone_condition = 1;
int interphone_resp_cnt = 0;
int interphone_condition_cnt = 0;

void fire_control(void);
int fire_high_flag = 0;
int fire_high_flag2 = 0;
int fire_low_flag = 0;
int fire_condition = 1;
int fire_condition_cnt = 0;

int cloud_send_cnt = 0;

unsigned char dht11_recv = 0;
unsigned int dht11_cnt = 0;

unsigned char power_signal_check = 1;
unsigned char door_signal_check = 1;
unsigned char interphone_signal_check = 1;
unsigned char earthquake_signal_check = 1;


unsigned char test_signal_check = 1;

float adc_data;


// gpio 출력함수
void gpio_write(int port, int value);



// 온습도 관련
unsigned char get_value(void);

uint32_t cfgcon_out, cfgcon_in;

float temp[10], humi[10];
float temp_calc, humi_calc = 200;
unsigned char temp_cnt, humi_cnt = 0;
int tmp;
void dht11_value(void);
unsigned short dht11_state = 0;



// 시간관련
char pingreq_time[8];
size_t sz_pingreq_time = sizeof(pingreq_time);
unsigned short pingreq_time_calc = 0;

char current_time[32];
size_t sz_current_time = sizeof(current_time);
unsigned short current_time_calc = 0;
unsigned short current_time_calc1 = 0;
struct timespec currentTime;
struct tm tm_now;



// http 출력 배열
char httpbody[100];

// mqtt 관련
void initializeConfigUtil(void);
char device_id[] = "9c8deecc68ba40bb89a1635f397c5f5e";
char device_token[] = "c3d520f749264fb8876dba577e439b36";



// security mode 인증서
static const char mqtt_ca_cert_str[] = \
		"-----BEGIN CERTIFICATE-----\r\n"
		"MIIGrTCCBZWgAwIBAgIQASAP9e8Tbenonqd/EQFJaDANBgkqhkiG9w0BAQsFADBN\r\n"
		"MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMScwJQYDVQQDEx5E\r\n"
		"aWdpQ2VydCBTSEEyIFNlY3VyZSBTZXJ2ZXIgQ0EwHhcNMTgwMzA4MDAwMDAwWhcN\r\n"
		"MjAwNDA1MTIwMDAwWjBzMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5p\r\n"
		"YTERMA8GA1UEBxMIU2FuIEpvc2UxJDAiBgNVBAoTG1NhbXN1bmcgU2VtaWNvbmR1\r\n"
		"Y3RvciwgSW5jLjEWMBQGA1UEAwwNKi5hcnRpay5jbG91ZDCCASIwDQYJKoZIhvcN\r\n"
		"AQEBBQADggEPADCCAQoCggEBANghNaTXWDfYV/JWgBnX4hmhcClPSO0onx5B2url\r\n"
		"YzpvTc3MBaQ+08YBpAKvTqZvPqrJUIM45Q91M301I5e2kz0DMq2zQZOGB0B83V/O\r\n"
		"O4vwETq4PCjAPhMinF4dN6HeJCuqo1CLh8evhfkFiJvpEfQWTxdjzPJ0Zdj/2U8E\r\n"
		"8Ht7zV5pWiDtuejtIDHB5H6fCx4xeQy/E+5l4V6R3BnRKpZsJtlhTh0RFqWhw5DJ\r\n"
		"/WWpGP//1VTZSHyW9SABsPd+jP1YgDraRD4b4lZBU6c8nC5qT3dhdiYoG6xUgTb3\r\n"
		"kfgUhhlOFpe3sBtR32OS8RuFrFeQDGaa3r6pfSy06Kph/eECAwEAAaOCA2EwggNd\r\n"
		"MB8GA1UdIwQYMBaAFA+AYRyCMWHVLyjnjUY4tCzhxtniMB0GA1UdDgQWBBSNBf6r\r\n"
		"7S/j0oV3A0XmEflXErutQDAlBgNVHREEHjAcgg0qLmFydGlrLmNsb3VkggthcnRp\r\n"
		"ay5jbG91ZDAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsG\r\n"
		"AQUFBwMCMGsGA1UdHwRkMGIwL6AtoCuGKWh0dHA6Ly9jcmwzLmRpZ2ljZXJ0LmNv\r\n"
		"bS9zc2NhLXNoYTItZzYuY3JsMC+gLaArhilodHRwOi8vY3JsNC5kaWdpY2VydC5j\r\n"
		"b20vc3NjYS1zaGEyLWc2LmNybDBMBgNVHSAERTBDMDcGCWCGSAGG/WwBATAqMCgG\r\n"
		"CCsGAQUFBwIBFhxodHRwczovL3d3dy5kaWdpY2VydC5jb20vQ1BTMAgGBmeBDAEC\r\n"
		"AjB8BggrBgEFBQcBAQRwMG4wJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRpZ2lj\r\n"
		"ZXJ0LmNvbTBGBggrBgEFBQcwAoY6aHR0cDovL2NhY2VydHMuZGlnaWNlcnQuY29t\r\n"
		"L0RpZ2lDZXJ0U0hBMlNlY3VyZVNlcnZlckNBLmNydDAJBgNVHRMEAjAAMIIBfwYK\r\n"
		"KwYBBAHWeQIEAgSCAW8EggFrAWkAdgCkuQmQtBhYFIe7E6LMZ3AKPDWYBPkb37jj\r\n"
		"d80OyA3cEAAAAWIHFb1dAAAEAwBHMEUCIQCQ0UjVVJSQDRB3oxzI5aD1Hs5GhbXj\r\n"
		"I6Cqt3/tkXT1WQIgNVWRgbJ72Ik9gp5QoNxhCZ+h//or0uL7PHnv3cP5L9UAdgBv\r\n"
		"U3asMfAxGdiZAKRRFf93FRwR2QLBACkGjbIImjfZEwAAAWIHFb73AAAEAwBHMEUC\r\n"
		"IQDxCxJCsZjuqbQvuwipgdUf1l6qXdiekM5zn33i1+KYxgIgKDMJEuKHzhkweT2S\r\n"
		"Y4dWBuzSdOAzZfoDrIGdsFvkxi0AdwC72d+8H4pxtZOUI5eqkntHOFeVCqtS6BqQ\r\n"
		"lmQ2jh7RhQAAAWIHFb1YAAAEAwBIMEYCIQCNDYdxWmqUGGwNzXlJ1/NXxzwqPYIB\r\n"
		"eSJDuR1xfWtSsQIhAJsygf2rqPS+O7qQAzggCQ2V/3JDRUhuxNDPqwooo47uMA0G\r\n"
		"CSqGSIb3DQEBCwUAA4IBAQBvRGWibvHFrRUWsArJ9lmS5MMZFbXXQPXbflgv3nSG\r\n"
		"ShmhBC3o+k97J0Wgp/wH7uDf01RrRMAVNm458g1Mr4AMAXq3zzxNNTwjGYw/USuG\r\n"
		"UprrKqc9onugtAUX8DGvlZr8SWO3FhPlyamWQ69jutx/X4nfHyZr41bX9WQ/ay0F\r\n"
		"GQJ1tRTrX1eUPO+ucXeG8vTbt09bRNnoY+i97dzrwHakXySfHohNsIbwmrsS4SQv\r\n"
		"7eG9g5+5vsc2B9ugGcELIYKrzDWNPshir37KSpcwLUCmDJkTQp8+KhJUKgbTALTa\r\n"
		"nxuDyNwZIwW66vv1t0Zi4vKU8hfUsAN2N3wcsb6pY/RA\r\n"
		"-----END CERTIFICATE-----\r\n"
		"-----BEGIN CERTIFICATE-----\r\n"
		"MIIElDCCA3ygAwIBAgIQAf2j627KdciIQ4tyS8+8kTANBgkqhkiG9w0BAQsFADBh\r\n"
		"MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\r\n"
		"d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\r\n"
		"QTAeFw0xMzAzMDgxMjAwMDBaFw0yMzAzMDgxMjAwMDBaME0xCzAJBgNVBAYTAlVT\r\n"
		"MRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxJzAlBgNVBAMTHkRpZ2lDZXJ0IFNIQTIg\r\n"
		"U2VjdXJlIFNlcnZlciBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\r\n"
		"ANyuWJBNwcQwFZA1W248ghX1LFy949v/cUP6ZCWA1O4Yok3wZtAKc24RmDYXZK83\r\n"
		"nf36QYSvx6+M/hpzTc8zl5CilodTgyu5pnVILR1WN3vaMTIa16yrBvSqXUu3R0bd\r\n"
		"KpPDkC55gIDvEwRqFDu1m5K+wgdlTvza/P96rtxcflUxDOg5B6TXvi/TC2rSsd9f\r\n"
		"/ld0Uzs1gN2ujkSYs58O09rg1/RrKatEp0tYhG2SS4HD2nOLEpdIkARFdRrdNzGX\r\n"
		"kujNVA075ME/OV4uuPNcfhCOhkEAjUVmR7ChZc6gqikJTvOX6+guqw9ypzAO+sf0\r\n"
		"/RR3w6RbKFfCs/mC/bdFWJsCAwEAAaOCAVowggFWMBIGA1UdEwEB/wQIMAYBAf8C\r\n"
		"AQAwDgYDVR0PAQH/BAQDAgGGMDQGCCsGAQUFBwEBBCgwJjAkBggrBgEFBQcwAYYY\r\n"
		"aHR0cDovL29jc3AuZGlnaWNlcnQuY29tMHsGA1UdHwR0MHIwN6A1oDOGMWh0dHA6\r\n"
		"Ly9jcmwzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbFJvb3RDQS5jcmwwN6A1\r\n"
		"oDOGMWh0dHA6Ly9jcmw0LmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbFJvb3RD\r\n"
		"QS5jcmwwPQYDVR0gBDYwNDAyBgRVHSAAMCowKAYIKwYBBQUHAgEWHGh0dHBzOi8v\r\n"
		"d3d3LmRpZ2ljZXJ0LmNvbS9DUFMwHQYDVR0OBBYEFA+AYRyCMWHVLyjnjUY4tCzh\r\n"
		"xtniMB8GA1UdIwQYMBaAFAPeUDVW0Uy7ZvCj4hsbw5eyPdFVMA0GCSqGSIb3DQEB\r\n"
		"CwUAA4IBAQAjPt9L0jFCpbZ+QlwaRMxp0Wi0XUvgBCFsS+JtzLHgl4+mUwnNqipl\r\n"
		"5TlPHoOlblyYoiQm5vuh7ZPHLgLGTUq/sELfeNqzqPlt/yGFUzZgTHbO7Djc1lGA\r\n"
		"8MXW5dRNJ2Srm8c+cftIl7gzbckTB+6WohsYFfZcTEDts8Ls/3HB40f/1LkAtDdC\r\n"
		"2iDJ6m6K7hQGrn2iWZiIqBtvLfTyyRRfJs8sjX7tN8Cp1Tm5gr8ZDOo0rwAhaPit\r\n"
		"c+LJMto4JQtV05od8GiG7S5BNO98pVAdvzr508EIDObtHopYJeS4d60tbvVS3bR0\r\n"
		"j6tJLp07kzQoH3jOlOrHvdPJbRzeXDLz\r\n"
		"-----END CERTIFICATE-----\r\n";


/**********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************
 *******************                                                 mqtt client                                *******************************************************************
 **********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************/



// mqtt client handle
mqtt_client_t* pClientHandle = NULL;

// mqtt client parameters
mqtt_client_config_t clientConfig;

//typedef struct _mqtt_tls_param_t {
//	const unsigned char *ca_cert;	/* CA certificate, common between client and MQTT Broker */
//	const unsigned char *cert;	/* Client certificate */
//	const unsigned char *key;	/* Client private key */
//	int ca_cert_len;			/* the length of CA certificate  */
//	int cert_len;				/* the length of Client certificate */
//	int key_len;				/* the length of key */
//} mqtt_tls_param_t;

mqtt_tls_param_t clientTls;

//int blinkerValue = 0;
int currentLED = 0;

// 시간이 잘못 설정되면 시간기간이 잘못설정되어 오류발생 artik053은 RTC가 없음 리부트 되면 2010년 1월1일로 리셋됨
// network time protocol(ntpc)
struct ntpc_server_conn_s g_server_conn[2];

const unsigned char *get_ca_cert(void) {
	return (const unsigned char*)mqtt_ca_cert_str;
}

// mqtt client on connect callback
void onConnect(void* client, int result) {
    printf("mqtt client connected to the server\n");
}

// mqtt client on disconnect callback
void onDisconnect(void* client, int result) {
    printf("mqtt client disconnected from the server\n");
}

// mqtt client on publish callback
void onPublish(void* client, int result) {
   printf("mqtt client Published message\n");
}



// mqtt client on message callback
void onMessage(void *client, mqtt_msg_t *msg) {
	int i;
	cJSON *jsonMsg = NULL;
	char *strActName = NULL;
	char *payload = strdup(msg->payload);

	printf("Received message\n");
    printf("Topic: %s\n", msg->topic);
    printf("Message: %s\n", payload);

    jsonMsg = cJSON_Parse((const char*)payload);
    cJSON *data = cJSON_GetObjectItem(jsonMsg, "actions");

    if (data == NULL) {
    	printf("data is null\n");
    	return;
    }

    //int dataLength = cJSON_GetArraySize(data);
    //printf("array length: %d\n", dataLength);

    cJSON *action = cJSON_GetArrayItem(data, 0);
    cJSON *actName = cJSON_GetObjectItem(action, "name");

    strActName = cJSON_Print(actName);

    cJSON_Delete(jsonMsg);
    printf("action name: %s\n", strActName);
    free(strActName);
    free(payload);

    if (strncmp(strActName, "\"setOn\"", 7) == 0)
    {
    	printf("Turn on gas valve\n");
    	// 가스밸브 열기
    	s5j_gpiowrite(gas_onoff, 0);
		up_mdelay(10);
		s5j_gpiowrite(gas_onoff, 1);
    }
    else if (strncmp(strActName, "\"setOff\"", 8) == 0)
    {
    	printf("Turn off gas valve\n");
    	// 가스밸브 닫기
    	s5j_gpiowrite(gas_onoff, 0);
		up_mdelay(100);
		s5j_gpiowrite(gas_onoff, 1);
    }
    else
    {
    	printf("Unrecognized action.\n");
    }
}


// Utility function to configure mqtt client
void initializeConfigUtil(void) {
    uint8_t macId[IFHWADDRLEN];
    int result = netlib_getmacaddr("wl1", macId);
    if (result < 0) {
        printf("Get MAC Address failed. Assigning \
                Client ID as 123456789");
        clientConfig.client_id =
                DEFAULT_CLIENT_ID; // MAC id Artik 053
    } else {
    printf("MAC: %02x:%02x:%02x:%02x:%02x:%02x\n",
            ((uint8_t *) macId)[0],
            ((uint8_t *) macId)[1], ((uint8_t *) macId)[2],
            ((uint8_t *) macId)[3], ((uint8_t *) macId)[4],
            ((uint8_t *) macId)[5]);
    char buf[12];
    sprintf(buf, "%02x%02x%02x%02x%02x%02x",
            ((uint8_t *) macId)[0],
            ((uint8_t *) macId)[1], ((uint8_t *) macId)[2],
            ((uint8_t *) macId)[3], ((uint8_t *) macId)[4],
            ((uint8_t *) macId)[5]);
    clientConfig.client_id = buf; // MAC id Artik 053
    printf("Registering mqtt client with id = %s\n", buf);
    }

    clientConfig.user_name = device_id;
    clientConfig.password = device_token;
    clientConfig.debug = true;
    clientConfig.on_connect = (void*) onConnect;
    clientConfig.on_disconnect = (void*) onDisconnect;
    clientConfig.on_message = (void*) onMessage;
    clientConfig.on_publish = (void*) onPublish;

    clientConfig.protocol_version = MQTT_PROTOCOL_VERSION_311;
    clientConfig.clean_session = true;

    clientTls.ca_cert = get_ca_cert();
    clientTls.ca_cert_len = sizeof(mqtt_ca_cert_str);
    clientTls.cert = NULL;
    clientTls.cert_len = 0;
    clientTls.key = NULL;
    clientTls.key_len = 0;

    clientConfig.tls = &clientTls;
}




static void ntp_link_error(void)
{
	printf("ntp_link_error() callback is called.\n");
}



/**********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************
 *******************                                                 gpio		                                *******************************************************************
 **********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************/
// Write the value of given gpio port.
void gpio_write(int port, int value) {
    char str[4];
    static char devpath[16];
    snprintf(devpath, 16, "/dev/gpio%d", port);
    int fd = open(devpath, O_RDWR);

    ioctl(fd, GPIOIOC_SET_DIRECTION, GPIO_DIRECTION_OUT);
    write(fd, str, snprintf(str, 4, "%d", value != 0) + 1);

    close(fd);
}


/**********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************
 *******************                                                 http		                                *******************************************************************
 **********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************/

artik_error test_http_post_temp_humid(void)
{
    artik_http_module *http = (artik_http_module *)artik_request_api_module("http");
    artik_error ret = S_OK;
    char *response = NULL;
    int status = 0;
    artik_http_headers headers;
    artik_http_header_field fields[] = {
    	{"Connect", "close"},
        {"user-agent", "Artik browser"},
        {"Accept-Language", "en-US,en;q=0.8"},
		//{"keep-alive", "yes"}

    };

    sprintf(httpbody, "temp=%.1f&humid=%1.f&temphumid_id=%i&device_id=%s", temp_calc, humi_calc, id_val, device_id);
    char address[] = "http://minsanggyu2.cafe24.com/temphumidinsert.php";
    headers.fields = fields;
    headers.num_fields = ARRAY_SIZE(fields);

    fprintf(stderr, "TEST: %s starting\n", address);
    	ret = http->post(address, &headers, httpbody, &response, &status, NULL);

    if (ret != S_OK) {
        fprintf(stderr, "TEST: %s failed (err=%s)\n", address, error_msg(ret));
        goto exit;
    }
    if (response) {
        fprintf(stderr, "HTTP %d - %s\n", status, response);
        free(response);
    }

    exit:
    artik_release_api_module(http);
    return (ret == S_OK);
}

artik_error test_http_post_motion(void)
{
	artik_http_module *http = (artik_http_module *)artik_request_api_module("http");
	artik_error ret = S_OK;
	char *response = NULL;
	int status = 0;
	artik_http_headers headers;
	artik_http_header_field fields[] = {
			{"Connect", "close"},
	        {"user-agent", "Artik browser"},
	        {"Accept-Language", "en-US,en;q=0.8"},
			{"keep-alive", "yes"}
	};

	sprintf(httpbody, "device_id=%s&motion=%i", device_id, motion_id);
	printf("motion_id : %i\n", motion_id);

    char address[] = "http://minsanggyu2.cafe24.com/motioninsert.php";
    headers.fields = fields;
    headers.num_fields = ARRAY_SIZE(fields);

    fprintf(stderr, "TEST: %s starting\n", address);
    ret = http->post(address, &headers, httpbody, &response, &status, NULL);

    if (ret != S_OK) {
    	fprintf(stderr, "TEST: %s failed (err=%s)\n", address, error_msg(ret));
    	goto exit;
    }
    if (response) {
    	fprintf(stderr, "HTTP %d - %s\n", status, response);
    	free(response);
    }

	exit:
	    artik_release_api_module(http);
	    return (ret == S_OK);

}

/**********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************
 *******************                                                 MAIN                                       *******************************************************************
 **********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************/
int main(int argc, FAR char *argv[])
{



	s5j_configgpio(fire_signal);
	s5j_configgpio(id_sw3);
	s5j_configgpio(emergency_signal);
	s5j_configgpio(id_sw1);
	s5j_configgpio(id_sw2);
	s5j_configgpio(gas_onoff);
	s5j_configgpio(gas_sig);
	s5j_configgpio(eq_int2);
	s5j_configgpio(interphone_signal);
	s5j_configgpio(door_signal);
	s5j_configgpio(eq_int1);
	s5j_configgpio(power_signal);
	s5j_configgpio(motion_a_signal);
	s5j_configgpio(motion_b_signal);
	s5j_configgpio(motion_c_signal);
	s5j_configgpio(motion_d_signal);

	//초기값 설정
	s5j_gpiowrite(gas_onoff, 1);

/**********************************************************************************************************************************************************************************
*******************                                                gpio input&output select                                       ************************************************
**********************************************************************************************************************************************************************************/
	cfgcon_out = GPIO_OUTPUT | GPIO_PORTG2 | GPIO_PIN6;
	cfgcon_in = GPIO_INPUT | GPIO_PORTG2 | GPIO_PIN6;


/**********************************************************************************************************************************************************************************
*******************                                                wifi	connect				                                       ************************************************
**********************************************************************************************************************************************************************************/

    bool wifiConnected = false;
    gpio_write(RED_ON_BOARD_LED, 1); // Turn on on board Red LED to indicate no WiFi connection is established

    char *strTopicMsg = (char*)malloc(sizeof(char)*256);
    char *strTopicAct = (char*)malloc(sizeof(char)*256);

    // 디바이스당 2개의 topic할당 publish는 메시지topic을 이용해서만 가능.
    sprintf(strTopicMsg, "/v1.1/messages/%s", device_id);
    sprintf(strTopicAct, "/v1.1/actions/%s", device_id);

    memset(&clientConfig, 0, sizeof(clientConfig));
    memset(&clientTls, 0, sizeof(clientTls));

    // for NTP Client
    memset(&g_server_conn, 0, sizeof(g_server_conn));
    g_server_conn[0].hostname = "0.asia.pool.ntp.org";
    g_server_conn[0].port = 123;
    g_server_conn[1].hostname = "1.asia.pool.ntp.org";
    g_server_conn[1].port = 123;

#ifdef CONFIG_CTRL_IFACE_FIFO
    int ret;

    while(!wifiConnected) {
        ret = mkfifo(CONFIG_WPA_CTRL_FIFO_DEV_REQ, CONFIG_WPA_CTRL_FIFO_MK_MODE);
        if (ret != 0 && ret != -EEXIST) {
            printf("mkfifo error for %s: %s", CONFIG_WPA_CTRL_FIFO_DEV_REQ, strerror(errno));
        }
        ret = mkfifo(CONFIG_WPA_CTRL_FIFO_DEV_CFM, CONFIG_WPA_CTRL_FIFO_MK_MODE);
        if (ret != 0 && ret != -EEXIST) {
            printf("mkfifo error for %s: %s", CONFIG_WPA_CTRL_FIFO_DEV_CFM, strerror(errno));
        }

        ret = mkfifo(CONFIG_WPA_MONITOR_FIFO_DEV, CONFIG_WPA_CTRL_FIFO_MK_MODE);
        if (ret != 0 && ret != -EEXIST) {
            printf("mkfifo error for %s: %s", CONFIG_WPA_MONITOR_FIFO_DEV, strerror(errno));
        }
    #endif

        if (start_wifi_interface() == SLSI_STATUS_ERROR) {
            printf("Connect Wi-Fi failed. Try Again.\n");
        }
        else {
            wifiConnected = true;
            gpio_write(RED_ON_BOARD_LED, 0); // Turn off Red LED to indicate WiFi connection is established
        }
    }

    printf("Connect to Wi-Fi success\n");

    bool mqttConnected = false;
    bool ipObtained = false;
    printf("Get IP address\n");

    struct dhcpc_state state;
    void *dhcp_handle;

    while(!ipObtained) {
        dhcp_handle = dhcpc_open(NET_DEVNAME);
        ret = dhcpc_request(dhcp_handle, &state);
        dhcpc_close(dhcp_handle);

        if (ret != OK) {
            printf("Failed to get IP address\n");
            printf("Try again\n");
            sleep(1);
        }
        else {
            ipObtained = true;
        }
    }
    //wifi 할당받음
    netlib_set_ipv4addr(NET_DEVNAME, &state.ipaddr);
    netlib_set_ipv4netmask(NET_DEVNAME, &state.netmask);
    netlib_set_dripv4addr(NET_DEVNAME, &state.default_router);

    printf("IP address  %s\n", inet_ntoa(state.ipaddr));

    up_mdelay(2000);
    //서버시간받아서 아틱기준시간 받겠다. 인증서 사용가능
    int ret_ntp = ntpc_start(g_server_conn, 2, 1000, ntp_link_error);
    printf("ret: %d\n", ret_ntp);

    // Connect to the WiFi network for Internet connectivity
    printf("mqtt client tutorial\n");

    // Initialize mqtt client
    initializeConfigUtil();

    pClientHandle = mqtt_init_client(&clientConfig);
    if (pClientHandle == NULL) {
        printf("mqtt client handle initialization fail\n");
        return 0;
    }

    while (mqttConnected == false ) {
        sleep(2);
        // Connect mqtt client to server
        int result = mqtt_connect(pClientHandle, SERVER_ADDR, SERVER_PORT, 60);

        if (result == 0) {

        	clock_gettime(CLOCK_REALTIME, &currentTime);
        	localtime_r((time_t*)&currentTime.tv_sec, &tm_now);
        	strftime(pingreq_time, sz_pingreq_time, "%S", &tm_now);
        	printf("pingreq time: %s\n", pingreq_time);
        	//ascii에서 10진수로 변환
        	pingreq_time_calc = (pingreq_time[0] & 0x0f) * 10;
        	pingreq_time_calc = (pingreq_time[1] & 0x0f) + pingreq_time_calc;

            mqttConnected = true;
            printf("mqtt client connected to server\n");
            break;
        } else {
            continue;
        }
    }

    bool mqttSubscribe = false;

    // Subscribe to topic of interest
	while (mqttSubscribe == false ) {
		sleep(2);
		int result = mqtt_subscribe(pClientHandle, strTopicAct, 0); //topic - color, QOS - 1
		if (result < 0) {
			printf("mqtt client subscribe to topic failed\n");
			continue;
		}
		mqttSubscribe = true;
		printf("mqtt client Subscribed to the topic successfully\n");
	}

 /**********************************************************************************************************************************************************************************
  **********************************************************************************************************************************************************************************
  *******************                                                 while                                      *******************************************************************
  **********************************************************************************************************************************************************************************
  **********************************************************************************************************************************************************************************/


    while(1)
    {


    	clock_gettime(CLOCK_REALTIME, &currentTime);
    	localtime_r((time_t*)&currentTime.tv_sec, &tm_now);
    	strftime(current_time, sz_current_time, "%S", &tm_now);

    	//ascii에서 10진수로 변환
    	current_time_calc1 = (current_time[0] & 0x0f) * 10;
    	current_time_calc1 = (current_time[1] & 0x0f) + current_time_calc1;

    	power_control();
    	door_control();
    	gas_control();
    	earthquake_control();
    	emergency_control();
    	interphone_control();
    	fire_control();
    	motion_control();


    	// 5분당 한번씩 cloud에 데이터 보내기
    	if(cloud_send_cnt > 299)
    	{
    		if(temp_calc == 200 && humi_calc == 200)
			{
    			char buf[600];

				sprintf(buf, "{\"power\":%i, \"door\":%i, \"gas\":%i, \"earthquake\":%i, \"emergency\":%i, \"interphone\":%i, \"fire\":%i, \"id\":%i, \"battery\":%.2f}",
								power_condition, door_condition, gas_condition, earthquake_condition, emergency_condition, interphone_condition, fire_condition, id_val, adc_data);
				char* msg = buf;
				// construct the mqtt message to be published
				mqtt_msg_t message;
				message.payload = (char*)buf;
				message.payload_len = strlen(buf);
				message.topic = strTopicMsg;
				message.qos = 0;
				message.retain = 0;

				mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);
			}
    		else
    		{
    			char buf[600];

				sprintf(buf, "{\"temp\":%.1f, \"humid\":%.1f, \"power\":%i, \"door\":%i, \"gas\":%i, \"earthquake\":%i, \"emergency\":%i, \"interphone\":%i, \"fire\":%i, \"id\":%i, \"battery\":%.2f}",
								temp_calc, humi_calc, power_condition, door_condition, gas_condition, earthquake_condition, emergency_condition, interphone_condition, fire_condition, id_val, adc_data);
				char* msg = buf;
				// construct the mqtt message to be published
				mqtt_msg_t message;
				message.payload = (char*)buf;
				message.payload_len = strlen(buf);
				message.topic = strTopicMsg;
				message.qos = 0;
				message.retain = 0;

				mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);
    		}
    		cloud_send_cnt = 0;
    	}

    	if(fire_high_flag2 >= 3)
		{
			fire_condition = 0;
			fire_high_flag2 = 0;
			printf("fire_signal_error\n");

			// cloud로 error 보내기
			char buf[600];

			sprintf(buf, "{\"fire\":%i, \"id\":%i}", fire_condition, id_val);
			char* msg = buf;
			// construct the mqtt message to be published
			mqtt_msg_t message;
			message.payload = (char*)buf;
			message.payload_len = strlen(buf);
			message.topic = strTopicMsg;
			message.qos = 0;
			message.retain = 0;

			mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);

		}

    	// 1초마다
    	if(current_time_calc1 != current_time_calc)
    	{

    		current_time_calc = current_time_calc1;
    		printf("Current time: %u\n", current_time_calc1);




    		cloud_send_cnt++;

    		// gas_onoff high상태 유지
    		s5j_gpiowrite(gas_onoff, 1);
    		id_control();

    		if(cloud_send_cnt == 30 || cloud_send_cnt == 90 || cloud_send_cnt == 150 || cloud_send_cnt == 210 || cloud_send_cnt == 270)
			{
				if(temp_calc != 200 && humi_calc != 200)
				{
					test_http_post_temp_humid();
				}
			}

    		// 전력상태가 비정상이면
    		if(power_flag == 0)
    		{
    			// 1초씩 증가
    			power_cnt++;
    			//printf("power_cnt : %i\n", power_cnt);
    			// 10초 이상이 되고 파워컨디션이 정상이면
    			if((power_cnt > 9) && (power_condition == 1))
    			{
    				// 비정상으로 만들기
    				power_condition = 0;
    				// cloud로 error 보내기
					char buf[600];

					sprintf(buf, "{\"power\":%i, \"id\":%i}", power_condition, id_val);
					char* msg = buf;
					// construct the mqtt message to be published
					mqtt_msg_t message;
					message.payload = (char*)buf;
					message.payload_len = strlen(buf);
					message.topic = strTopicMsg;
					message.qos = 0;
					message.retain = 0;

					mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);
    				printf("power_error\n");
    			}
    			if(power_condition == 0)
    			{
    				power_cnt = 60;
    			}
    		}

    		// 문열림 상태가 비정상이면
    		if(door_flag == 0)
    		{
    			// 1초씩 증가
    			door_cnt++;
    			//printf("door_cnt : %i\n", door_cnt);
    			// 20초 이상이 되고 문열림상태가 정상이면
    			if((door_cnt > 19) && (door_condition == 1))
    			{
    				// 비정상으로 만들기
    				door_condition = 0;
    				printf("door_error\n");


    				// cloud로 error 보내기
    				char buf[600];

					sprintf(buf, "{\"door\":%i, \"id\":%i}", door_condition, id_val);
					char* msg = buf;
					// construct the mqtt message to be published
					mqtt_msg_t message;
					message.payload = (char*)buf;
					message.payload_len = strlen(buf);
					message.topic = strTopicMsg;
					message.qos = 0;
					message.retain = 0;

					mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);
    			}
    			if(door_condition == 0)
    			{
    				door_cnt = 60;
    			}
    		}

    		// 가스밸브 열림상태이면(비정상)
    		if(gas_flag == 0)
    		{
    			// 1초씩 증가
    			gas_cnt++;
    			//printf("gas_cnt : %i\n", gas_cnt);
    			// 3초 이상이 되고 가스밸브 상태가 정상이면
    			if((gas_cnt > 2) && (gas_condition == 1))
    			{
    				// 비정상으로 만들기
    				gas_condition = 0;
    				printf("gas_open\n");

    				// cloud로 error 보내기
        			char buf[600];

    				sprintf(buf, "{\"gas\":%i, \"id\":%i}", gas_condition, id_val);
    				char* msg = buf;
    				// construct the mqtt message to be published
    				mqtt_msg_t message;
    				message.payload = (char*)buf;
    				message.payload_len = strlen(buf);
    				message.topic = strTopicMsg;
    				message.qos = 0;
    				message.retain = 0;

    				mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);

    			}
    			if(gas_condition == 0)
    			{
    				gas_cnt = 60;

    			}
    		}
    		if(gas_flag == 1 && gas_condition == 0)
    		{
    			gas_condition = 1;

    			// cloud로 error 보내기
				char buf[600];

				sprintf(buf, "{\"gas\":%i, \"id\":%i}", gas_condition, id_val);
				char* msg = buf;
				// construct the mqtt message to be published
				mqtt_msg_t message;
				message.payload = (char*)buf;
				message.payload_len = strlen(buf);
				message.topic = strTopicMsg;
				message.qos = 0;
				message.retain = 0;

				mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);


    		}
    		// 진도 5 이하 지진이 발생하면(비정상)
    		if((earthquake_flag == 0) && (s5j_gpioread(eq_int2) == 1))
    		{
    			// 1초씩 증가
    			earthquake_cnt++;
    			//printf("earthquake_cnt : %i\n", earthquake_cnt);
    			// 3초 이상이 되고 지진상태가 정상이면
    			if((earthquake_cnt > 2) && (earthquake_condition == 1))
    			{
    				// 비정상으로 만들기
    				earthquake_condition = 0;
    				// cloud로 error 보내기
					char buf[600];

					sprintf(buf, "{\"earthquake\":%i, \"id\":%i}", earthquake_condition, id_val);
					char* msg = buf;
					// construct the mqtt message to be published
					mqtt_msg_t message;
					message.payload = (char*)buf;
					message.payload_len = strlen(buf);
					message.topic = strTopicMsg;
					message.qos = 0;
					message.retain = 0;

					mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);
    				//printf("earthquake_error");
    			}
    			// 비정상상태이면 60초 유지(한번만 발송)
    			if(earthquake_condition == 0)
    			{
    				earthquake_cnt = 60;
    			}
    		}

    		// 진도 5 이상 지진이 발생하면
    		if(earthquake_send == 0 && earthquake_condition == 2)
			{
				printf("earthquake_error진도5\n");
				// cloud로 error 보내기
				char buf[600];

				sprintf(buf, "{\"earthquake\":%i, \"id\":%i}", earthquake_condition, id_val);
				char* msg = buf;
				// construct the mqtt message to be published
				mqtt_msg_t message;
				message.payload = (char*)buf;
				message.payload_len = strlen(buf);
				message.topic = strTopicMsg;
				message.qos = 0;
				message.retain = 0;

				mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);
				// 한번만 전송
				earthquake_send = 1;
			}

    		if(emergency_condition == 0)
    		{
    			emergency_cnt++;
    			if(emergency_cnt == 3)
    			{
    				printf("emergency_emergency_emergency\n");
    				// cloud로 error 보내기
        			char buf[600];

    				sprintf(buf, "{\"emergency\":%i, \"id\":%i}", emergency_condition, id_val);
    				char* msg = buf;
    				// construct the mqtt message to be published
    				mqtt_msg_t message;
    				message.payload = (char*)buf;
    				message.payload_len = strlen(buf);
    				message.topic = strTopicMsg;
    				message.qos = 0;
    				message.retain = 0;

    				mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);

    			}
    			//printf("emergency_cnt : %i\n", emergency_cnt);
    		}

    		if(interphone_condition == 2)
			{
				// 인터폰 응답시간 카운트
				interphone_resp_cnt++;
				// 10초 동안 응답이 없으면 비정상
				if(interphone_resp_cnt > 9)
				{
					interphone_condition = 0;
					interphone_resp_cnt = 0;
					printf("interphone do not response\n");

					// cloud로 error 보내기
					char buf[600];

					sprintf(buf, "{\"interphone\":%i}", interphone_condition);
					char* msg = buf;
					// construct the mqtt message to be published
					mqtt_msg_t message;
					message.payload = (char*)buf;
					message.payload_len = strlen(buf);
					message.topic = strTopicMsg;
					message.qos = 0;
					message.retain = 0;

					mqtt_publish(pClientHandle, message.topic, (char*)message.payload, message.payload_len, message.qos, message.retain);

					interphone_condition = 1;
				}
			}
    		// 인터폰 받음
    		if(interphone_condition == 3)
			{
    			if(s5j_gpioread(interphone_signal) == 1)
    			{
    				interphone_condition_cnt++;
    				//인터폰 끊고 5초뒤에 복귀
    				if(interphone_condition_cnt >= 5)
    				{
    					interphone_condition = 1;
    					interphone_condition_cnt = 0;
    				}
    			}
    			else if(s5j_gpioread(interphone_signal) == 0)
    			{
    				interphone_condition_cnt = 0;
    			}
			}


			// 화재 원상복구 20초
			if(fire_condition == 0)
			{
				fire_condition_cnt++;
				if(fire_condition_cnt >= 20)
				{
					fire_condition = 1;
					fire_condition_cnt = 0;
				}
			}

			if((current_time_calc % 2) == 0)
			{

				analogInit();

		        adc_data = (analogRead(2) * 3.3) / 4096;

				// 저항비 = 1K : 6.6K
				// 값에 1.15 * 2 해야 정확한 배터리의 전압값 측정됌.
				adc_data = adc_data * 2.3;



				printf("Read ADC Pin Value: %.2f\n", adc_data);
				analogFinish();


			}
    		if((current_time_calc % 3) == 0)
    		{
    			//close(adc_fd);
    			dht11_value();


    		}
    	}
    }

}


void power_control(void)
{
	// 전력이 정상이면
	if(s5j_gpioread(power_signal) == 1)
	{
		// 시간초기화
		power_cnt = 0;
		// 전력상태 정상
		power_flag = 1;
		// 전력상태 정상
		power_condition = 1;
	}
	// 전력이 차단되면(비정상)
	else if(s5j_gpioread(power_signal) == 0)
	{
		// 전력상태 비정상
		power_flag = 0;
	}
}

void door_control(void)
{
	// 문이 닫혀있으면(정상)
	if(s5j_gpioread(door_signal) == 0)
	{
		// 시간초기화
		door_cnt = 0;
		// 문열림상태 정상
		door_flag = 1;
		// 문열림상태 정상
		door_condition = 1;
	}
	// 문이 열려있으면(비정상)
	else if(s5j_gpioread(door_signal) == 1)
	{
		// 문열림 비정상
		door_flag = 0;
	}
}

void gas_control(void)
{
	// 가스밸브가 닫혀있으면(정상)
	if(s5j_gpioread(gas_sig) == 0)
	{
		gas_cnt = 0;
		gas_flag = 1;
		//gas_condition = 1;
	}
	// 가스밸브가 열려있으면
	else if(s5j_gpioread(gas_sig) == 1)
	{
		gas_flag = 0;
	}
}

void earthquake_control(void)
{
	// 지진이 발생하지 않으면(정상)
	if((s5j_gpioread(eq_int1) == 1) && (s5j_gpioread(eq_int2) == 1))
	{
		// 시간 초기화
		earthquake_cnt = 0;
		// 진도 5이상일때 한번보내는것 초기화
		earthquake_send = 0;
		earthquake_flag = 1;
		earthquake_condition = 1;
	}
	// 진도 5이하 지진 발생시(비정상)
	if(s5j_gpioread(eq_int1) == 0)
	{
		earthquake_flag = 0;
		// 진도 5이상 지진 발생시(비정상)
		if(s5j_gpioread(eq_int2) == 0)
		{
			earthquake_condition = 2;
		}
	}
}

void emergency_control(void)
{
	// 스위치가 눌리지 않고 비정상에서 10초 이상 지나면(정상)
	if((s5j_gpioread(emergency_signal) == 0) && (emergency_cnt > 9))
	{
		// 시간 초기화
		emergency_cnt = 0;
		emergency_condition = 1;
		printf("emergency_reset\n");
	}
	// 스위치가 눌리면
	else if(s5j_gpioread(emergency_signal) == 1)
	{
		emergency_condition = 0;

	}
}

void interphone_control(void)
{
	if(s5j_gpioread(interphone_signal) == 1)
	{
		//printf("interphone_signal = 1\n");
		if(interphone_flag < 200 && interphone_flag > 10 && interphone_condition == 1)
		{
			//받음3, 벨누름 2, 정상 1, 비정상 0
			interphone_condition = 2;
			printf("interphone bell push\n");
		}

		interphone_flag = 0;
	}
	if(s5j_gpioread(interphone_signal) == 0)
	{
		up_mdelay(1);
		interphone_flag++;

		if(interphone_flag > 1000)
		{
			interphone_flag = 0;
			// 인터폰 받음
			if(interphone_condition == 2)
			{
				interphone_condition = 3;
				interphone_resp_cnt = 0;
				printf("interphone response complete\n");
			}
		}
	}
}

void fire_control(void)
{
	if(s5j_gpioread(fire_signal) == 1)
	{
		up_mdelay(1);
		fire_high_flag++;
	}
	else if(s5j_gpioread(fire_signal) == 0)
	{
		up_mdelay(1);
		fire_low_flag++;
		if(fire_low_flag > 10000)
		{
			fire_high_flag2 = 0;
			fire_low_flag = 0;
		}
		if(fire_high_flag > 500)
		{
			fire_high_flag2++;
			fire_low_flag = 0;
		}
		fire_high_flag = 0;
	}
}

void motion_control(void)
{
	// motion a 상태
	// 움직임이 시작될때
//	if((s5j_gpioread(motion_a_signal) == 1) && (motion_a_situation == 0))
//	{
//		motion_a_situation = 1;
//		// http 서버로 전송시 모션 id(0) 설정
//		motion_id = id_val << 2;
//		test_http_post_motion();
//	}
//	// 움직임이 끝날때
//	if((s5j_gpioread(motion_a_signal) == 0) && (motion_a_situation == 1))
//	{
//		motion_a_situation = 0;
//	}

	// motion b 상태
	// 움직임이 시작될때
	if((s5j_gpioread(motion_b_signal) == 1) && (motion_b_situation == 0))
	{
		motion_b_situation = 1;
		// http 서버로 전송시 모션 id(1) 설정
		motion_id = (id_val << 2) | 1;
		test_http_post_motion();
	}
	// 움직임이 끝날때
	if((s5j_gpioread(motion_b_signal) == 0) && (motion_b_situation == 1))
	{
		motion_b_situation = 0;
	}

	// motion c 상태
	// 움직임이 시작될때
	if((s5j_gpioread(motion_c_signal) == 1) && (motion_c_situation == 0))
	{
		motion_c_situation = 1;
		// http 서버로 전송시 모션 id(2) 설정
		motion_id = (id_val << 2) | 2;
		test_http_post_motion();
	}
	// 움직임이 끝날때
	if((s5j_gpioread(motion_c_signal) == 0) && (motion_c_situation == 1))
	{
		motion_c_situation = 0;
	}

	// motion d 상태
	// 움직임이 시작될때
	if((s5j_gpioread(motion_d_signal) == 1) && (motion_d_situation == 0))
	{
		motion_d_situation = 1;
		// http 서버로 전송시 모션 id(3) 설정
		motion_id = (id_val << 2) | 3;
		test_http_post_motion();
	}
	// 움직임이 끝날때
	if((s5j_gpioread(motion_d_signal) == 0) && (motion_d_situation == 1))
	{
		motion_d_situation = 0;
	}
}

void id_control(void)
{
	id_sw1_val = !(s5j_gpioread(id_sw1));
	id_sw2_val = !(s5j_gpioread(id_sw2)) << 1;
	id_sw3_val = !(s5j_gpioread(id_sw3)) << 2;
	id_val = id_sw1_val | id_sw2_val | id_sw3_val;
	//printf("id_val : %i\n", id_val);
}



/**********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************
 *******************                                                온숩도센서		                                *******************************************************************
 **********************************************************************************************************************************************************************************
 **********************************************************************************************************************************************************************************/

void dht11_value(void)
{
	unsigned char data[5];
	int i, j, val,val1,val2;
	s5j_configgpio(cfgcon_out);

	s5j_gpiowrite(cfgcon_out, 1);
	up_mdelay(1);

	s5j_gpiowrite(cfgcon_out, 0);
	up_mdelay(1); // for DHT11

	s5j_configgpio(cfgcon_in);

	s5j_gpiowrite(cfgcon_out, 1);
	up_udelay(20);

	data[0] = data[1] = data[2] = data[3] = data[4] = 0;

	up_udelay(160);

	for (i = 0; i < 5; i++)
	{
		for (j = 0; j < 8; j++)
		{
			if (j < 7)
			{
				data[i] |= get_value();
				data[i] = data[i] << 1;
	    	}
			else
			{
				data[i] |= get_value();
	    	}
	    }
	}

	tmp = 0;

	for (i = 0; i < 4; i++)
	{
		tmp += data[i];
	}
	if (data[4] == (tmp & 0xFF))
	{
		val1 = data[0];
		val1 <<= 8;
		humi[humi_cnt++] = (float)(val1 + data[1]) / 10.0;


		val2 = data[2];
		val2 <<= 8;
		temp[temp_cnt++] = (float)(val2 + data[3]) / 10.0;

		if(temp_cnt == 9)
		{
			temp_cnt = 0;
			temp_calc = (temp[0] + temp[1] + temp[2] + temp[3] + temp[4] + temp[5] + temp[6] + temp[7] + temp[8] + temp[9]) / 10;
		}
		if(humi_cnt == 9)
		{
			humi_cnt = 0;
			humi_calc = (humi[0] + humi[1] + humi[2] + humi[3] + humi[4] + humi[5] + humi[6] + humi[7] + humi[8] + humi[9]) / 10;
		}
		// 값이 오류날경우
		if((temp_calc < -30) || (temp_calc > 100) || (humi_calc < 0) || (humi_calc > 100))
		{
			temp_calc = 200;
			humi_calc = 200;
		}
		printf("Temperature: %.1f, Humidity: %.1f\n", temp_calc, humi_calc);

	}
	else
	{
		printf("Parity Error\n");
		printf("data[0]: %d, data[1]: %d, data[2]: %d, data[3]: %d, \
	                  data[4]: %d\n", data[0], data[1], data[2], data[3], data[4]);
	}
}

unsigned char get_value(void)
{
	unsigned char va = 0;
	int state;

	while (s5j_gpioread(cfgcon_in) == 0);

	up_udelay(40);

	if (s5j_gpioread(cfgcon_in) == 1)
	{
	    va |= 1;
	    dht11_recv = s5j_gpioread(cfgcon_in);
	    while (dht11_recv == 1)
	    {
	    	dht11_cnt++;
	    	up_udelay(1);
	    	dht11_recv = s5j_gpioread(cfgcon_in);
	    	if(dht11_cnt >= 1000)
	    	{
	    		dht11_cnt = 0;
	    		dht11_recv = 0;
	    	}
	    }
	    dht11_recv = 0;
	}
	return va;
}



//=====================================ADC function=====================================
int analogInit(void){
	sample = (struct adc_msg_s *)malloc(sizeof(struct adc_msg_s)*S5J_ADC_MAX_CHANNELS);
	adc_fd = open("/dev/adc0", O_RDONLY);
	if (adc_fd < 0) {
		return FALSE;
	}
	return TRUE;
}

void analogFinish(void){
	close(adc_fd);
	free(sample);
}


int analogRead(int port){
	int result = -1;
	if(port >= 0 && port <= MAX_PIN_NUM){				//Check available ADC port
		int  ret;
		size_t readsize;
		ssize_t nbytes;

		while(1){
			ret = ioctl(adc_fd, ANIOC_TRIGGER, 0);
			if (ret < 0) {
				analogFinish();
				analogInit();
				return result;
			}

			readsize = S5J_ADC_MAX_CHANNELS * sizeof(struct adc_msg_s);
			nbytes = read(adc_fd, sample, readsize);
			if (nbytes < 0) {
				if (errno != EINTR) {
					analogFinish();
					analogInit();
					return result;
				}
			} else if (nbytes == 0) {

			} else {
				int nsamples = nbytes / sizeof(struct adc_msg_s);
				if (nsamples * sizeof(struct adc_msg_s) == nbytes) {
					int i;
					for (i = 0; i < nsamples; i++) {
						if(sample[i].am_channel ==port){
							result =  sample[i].am_data;
							goto out;
						}
					}
				}
			}
		}
		out:
		up_mdelay(5);
	}
	return result;
}

//=====================================PWM function=====================================
int pwmInit(int port, int frequency, int duty){
	static char devpath[16];
	snprintf(devpath, 16, "/dev/pwm%d", port);
	pwm_fd[port] = open(devpath, O_RDWR);
	if (pwm_fd[port] < 0) {
		return FALSE;
	}
	pwm_info[port].frequency = frequency;
	pwm_info[port].duty = duty * 65536 / 100;
	ioctl(pwm_fd[port], PWMIOC_SETCHARACTERISTICS, (unsigned long)((uintptr_t)&pwm_info[port]));
	ioctl(pwm_fd[port], PWMIOC_START);
	return TRUE;
}

void pwmSet(int port, int duty){
	pwm_info[port].duty = duty * 65536 / 100;
	ioctl(pwm_fd[port], PWMIOC_SETCHARACTERISTICS, (unsigned long)((uintptr_t)&pwm_info[port]));
	ioctl(pwm_fd[port], PWMIOC_START);
}

void pwmFinish(int port){
	ioctl(pwm_fd[port], PWMIOC_STOP);
	close(pwm_fd[port]);
}


