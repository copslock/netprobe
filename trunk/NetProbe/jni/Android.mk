# This makefile supplies the rules for building a library of JNI codefor
#use by our example of how to bundleashared library with an APK.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS) 

include Android-libpcap.mk
include Android-libjpcap.mk
#include Android-libjnetpcap.mk
#include Android-tcpdump.mk