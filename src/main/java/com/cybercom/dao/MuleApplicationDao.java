package com.cybercom.dao;

import com.cybercom.dao.objects.MuleApplication;

import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 1:57 PM
 */
public interface MuleApplicationDao {

    void createMuleApplication(MuleApplication muleApplication);

    MuleApplication getMuleApplication(int muleApplicationId);

    List<MuleApplication> getMuleApplicationByServerId(int muleServerId);

    boolean applicationExists(String applicationName, int muleServerId);

    void updateMuleApplication(MuleApplication muleApplication);

    void removeMuleApplication(int muleApplicationId);

    List<String> getMuleApplications();

    void removeMuleApplication(String applicationName);
}
