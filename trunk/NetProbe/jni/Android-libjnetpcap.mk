LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE:= libjnetpcap
LOCAL_SRC_FILES:=\
	jnetpcap/jnetpcap.cpp \
    jnetpcap/jnetpcap_beta.cpp \
    jnetpcap/jnetpcap_bpf.cpp \
    jnetpcap/jnetpcap_dumper.cpp \
    jnetpcap/jnetpcap_ids.cpp \
    jnetpcap/jnetpcap_pcap100.cpp \
    jnetpcap/jnetpcap_pcap_header.cpp \
    jnetpcap/jnetpcap_utils.cpp \
    jnetpcap/jpacket_buffer.cpp \
    jnetpcap/library.cpp \
    jnetpcap/mac_addr_dlpi.c \
    jnetpcap/mac_addr_sys.c \
    jnetpcap/nio_jbuffer.cpp \
    jnetpcap/nio_jmemory.cpp \
    jnetpcap/nio_jnumber.cpp \
    jnetpcap/packet_flow.cpp \
    jnetpcap/packet_jheader.cpp \
    jnetpcap/packet_jheader_scanner.cpp \
    jnetpcap/packet_jpacket.cpp \
    jnetpcap/packet_jscan.cpp \
    jnetpcap/packet_jsmall_scanner.cpp \
    jnetpcap/packet_protocol.cpp \
    jnetpcap/util_checksum.cpp \
    jnetpcap/util_crc16.c \
    jnetpcap/util_crc32.c \
    jnetpcap/util_debug.cpp \
    jnetpcap/util_in_cksum.cpp \
    jnetpcap/winpcap_ext.cpp \
    jnetpcap/winpcap_ids.cpp \
    jnetpcap/winpcap_send_queue.cpp \
    jnetpcap/winpcap_stat_ex.cpp \
	
LOCAL_C_INCLUDES += \
		$(LOCAL_PATH)/jnetpcap \
		$(LOCAL_PATH)/pcap \

LOCAL_CFLAGS += -O2 -g
LOCAL_LDFLAGS += -DHAVE_CONFIG_H -D_U_="__attribute__((unused))" -Dlinux -D__GLIBC__ -D_GNU_SOURCE
#LOCAL_LDFLAGS += $(LOCAL_PATH)/../obj/local/armeabi/libpcap.a
LOCAL_LDLIBS := $(LOCAL_PATH)/../obj/local/armeabi/libpcap.a 

include $(BUILD_SHARED_LIBRARY)
