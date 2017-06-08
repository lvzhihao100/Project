package com.eqdd.imrong.rongyun;

import java.util.List;

import cn.rongcloud.contactcard.ContactCardPlugin;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by lvzhihao on 17-4-30.
 */

public class MyExtensionModel extends DefaultExtensionModule {
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
//        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
//        FilePlugin file = new FilePlugin();
//        pluginModules.add(file);
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        pluginModules.add(new MingpianPluginModel());
        pluginModules.add(new ContactCardPlugin());
//        pluginModules.add(new videoplu());
        return pluginModules;
    }
}
