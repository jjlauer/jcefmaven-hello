package org.example;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
import org.cef.CefApp;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create a new CefAppBuilder instance
        CefAppBuilder builder = new CefAppBuilder();

        // Configure the builder instance
        builder.setInstallDir(new File("jcef-bundle")); //Default
        builder.setProgressHandler(new ConsoleProgressHandler()); //Default

        builder.addJcefArgs("--disable-gpu");
        // this fixes tearing (odd resizing bug in JCEF v78)
        builder.addJcefArgs("--disable-gpu-compositing");
        // this fixes not being able to execute javascript in child frame!
        builder.addJcefArgs("--disable-site-isolation-trials");
        // this *may* prevent a crash on Ubuntu 20.04 with seccomp issue
        // https://forum.endeavouros.com/t/chromium-based-browser-crashes-solution/3595/13
        builder.addJcefArgs("--disable-seccomp-filter-sandbox");
        // allow access to system clipboard?
        // these generally do not work w/ offscreen rendering
        builder.addJcefArgs("--use-system-clipboard");
        builder.addJcefArgs("--allow-no-sandbox-job");


        //builder.addJcefArgs("--disable-gpu"); //Just an example
        //builder.getCefSettings().windowless_rendering_enabled = true; //Default - select OSR mode

        // Set an app handler. Do not use CefApp.addAppHandler(...), it will break your code on MacOSX!
        builder.setAppHandler(new MavenCefAppHandlerAdapter(){});

        // Build a CefApp instance using the configuration above
        CefApp app = builder.build();

        // The simple example application is created as anonymous class and points
        // to Google as the very first loaded page. Windowed rendering mode is used by
        // default. If you want to test OSR mode set |useOsr| to true and recompile.
        boolean useOsr = true;
        MainFrame frame = new MainFrame(app, "http://www.google.com", useOsr, false);

        Thread.sleep(5000L);

        frame.setSize(1000, 800);
    }
}