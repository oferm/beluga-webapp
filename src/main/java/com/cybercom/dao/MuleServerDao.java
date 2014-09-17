package com.cybercom.dao;

import com.cybercom.dao.objects.MuleServer;

import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 10:26 AM
 */
public interface MuleServerDao {
    void createMuleServer(MuleServer muleServer);

    MuleServer getMuleServer(int id);

    MuleServer getMuleServer(String muleServerName);

    void updateMuleServer(MuleServer muleServer);

    List<MuleServer> getAllMuleServers();
}
