package com.boco.bomc.vpn.action;

import com.google.inject.Binder;
import com.google.inject.Module;


public class VpnModule implements Module {

    public void configure(Binder binder) {
        binder.bind(LoginAction.class).asEagerSingleton();
        binder.bind(AdviceAction.class).asEagerSingleton();
        binder.bind(UserAction.class).asEagerSingleton();
    }
    
}