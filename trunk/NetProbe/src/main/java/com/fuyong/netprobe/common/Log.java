package com.fuyong.netprobe.common;

import com.fuyong.netprobe.MyAppDirs;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: democrazy
 * Date: 13-6-16
 * Time: 下午5:41
 * To change this template use File | Settings | File Templates.
 */
public class Log {
    public static final String MY_APP = "log";
    public static final String CRASH_REPORT = "CrashReport";
    public static Logger logger = getLogger(MY_APP);

    public static Logger getLogger(String type) {
        return LogManager.getLogger(type);
    }

    public static void init() {
        LogManager.getLoggerRepository().resetConfiguration();
        initMyAppLog();
        initCrashLog();
    }

    private static void initMyAppLog() {
        final Logger logger = getLogger(MY_APP);
        final DailyRollingFileAppender dailyRollingFileAppender;
        final Layout fileLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p|%t|:%m%n");
        try {
            String filePath = MyAppDirs.getLogDir() + TelephonyUtil.getDeviceId() + "-" + MY_APP;
            dailyRollingFileAppender = new DailyRollingFileAppender(fileLayout, filePath, "yyyy-MM-dd");
            dailyRollingFileAppender.setImmediateFlush(true);
            logger.addAppender(dailyRollingFileAppender);
        } catch (final IOException e) {
            throw new RuntimeException("Exception configuring log system", e);
        }
    }

    private static void initCrashLog() {
        final Logger logger = getLogger(CRASH_REPORT);
        final DailyRollingFileAppender dailyRollingFileAppender;
        final Layout fileLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p|%t|:%m%n");
        try {
            String filePath = MyAppDirs.getLogDir() + TelephonyUtil.getDeviceId() + "-" + CRASH_REPORT;
            dailyRollingFileAppender = new DailyRollingFileAppender(fileLayout, filePath, "yyyy-MM-dd");
            dailyRollingFileAppender.setImmediateFlush(true);
            logger.addAppender(dailyRollingFileAppender);
        } catch (final IOException e) {
            throw new RuntimeException("Exception configuring log system", e);
        }
    }

    //
//    private static void initErrorLog() {
//        final Logger logger = getLogger(CRASH);
//        final RollingFileAppender rollingFileAppender;
//        final Layout fileLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p/%t/%C:%m%n");
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            rollingFileAppender = new RollingFileAppender(fileLayout, MyAppDirs.getLogDir() + "error" + df.formatNumber(new Date()) + ".log");
//        } catch (final IOException e) {
//            throw new RuntimeException("Exception configuring log system", e);
//        }
//
//        rollingFileAppender.setMaxBackupIndex(10);
//        rollingFileAppender.setMaximumFileSize(1024 * 1024);
//        rollingFileAppender.setImmediateFlush(true);
//
//        logger.addAppender(rollingFileAppender);
//    }
    public static void e(String cls, Throwable e) {
        StringWriter stack = new StringWriter();
        e.printStackTrace(new PrintWriter(stack));
        LogManager.getLogger(MY_APP).error("[" + cls + "] " + stack.toString());
    }

    public static void trace(String s) {
        logger.trace(s);
    }

    public static void debug(String s) {
        logger.debug(s);
    }

    public static void info(String s) {
        logger.info(s);
    }

    public static void error(String s) {
        logger.error(s);
    }

    public static void fatal(String s) {
        logger.fatal(s);
    }
}
