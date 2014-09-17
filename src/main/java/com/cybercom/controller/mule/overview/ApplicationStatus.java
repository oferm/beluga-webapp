package com.cybercom.controller.mule.overview;

import com.cybercom.dao.objects.MuleApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/31/13
 * Time: 1:43 PM
 */
public class ApplicationStatus {
    private MuleApplication muleApplication;
    private List<Status> listOfServerStatus;

    public ApplicationStatus(final MuleApplication muleApplication) {
        this.muleApplication = muleApplication;
        this.listOfServerStatus = new ArrayList<>();
    }

    public List<Status> getListOfServerStatus() {
        return listOfServerStatus;
    }

    public MuleApplication getMuleApplication() {
        return muleApplication;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ApplicationStatus that = (ApplicationStatus) o;

        if (!getMuleApplication().getName().equals(that.getMuleApplication().getName())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getMuleApplication().hashCode();
    }

    public enum Status {
        RUNNING("Running"),
        NOT_RUNNING("Not running"),
        RUNNING_NOT_IN_DATABASE("Running. Not in database");

        private String status;

        private Status(final String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }
}
