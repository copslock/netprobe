LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE:= libjpcap
LOCAL_SRC_FILES:=\
	jpcap/process.cpp\
	jpcap/jpcap.cpp\
	
LOCAL_C_INCLUDES += \
		$(LOCAL_PATH)/jpcap\
		$(LOCAL_PATH)/pcap

LOCAL_CFLAGS := -O2 -g
#LOCAL_LDFLAGS += $(LOCAL_PATH)/../obj/local/armeabi/libpcap.a
LOCAL_LDLIBS := $(LOCAL_PATH)/../obj/local/armeabi/libpcap.a 

include $(BUILD_SHARED_LIBRARY)
