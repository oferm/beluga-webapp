package com.cybercom.dao.impl;

import com.cybercom.DaoUtils;
import com.cybercom.dao.MuleApplicationDao;
import com.cybercom.dao.objects.MuleApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 2:00 PM
 */
public class MuleApplicationDaoImpl implements MuleApplicationDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(MuleApplicationDaoImpl.class);

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createMuleApplication(final MuleApplication muleApplication) {
        jdbcTemplate.update("INSERT INTO mule.application(name, muleServerId)"
                + "VALUES(?, ?)", mapMuleApplicationForInsert(muleApplication));
    }

    private Object[] mapMuleApplicationForInsert(final MuleApplication muleApplication) {
        Object[] result = new Object[2];
        int i = 0;
        result[i++] = muleApplication.getName();
        result[i] = muleApplication.getMuleServerId();
        return result;
    }

    @Override
    public MuleApplication getMuleApplication(final int muleApplicationId) {
        final List<MuleApplication> muleApplicationList = jdbcTemplate.query(
                "SELECT * FROM mule.application WHERE id = ?", new MuleApplicationMapper(), muleApplicationId);
        MuleApplication result = null;
        if (!muleApplicationList.isEmpty()) {
            result = muleApplicationList.get(0);
        }
        return result;
    }

    @Override
    public List<MuleApplication> getMuleApplicationByServerId(final int muleServerId) {
        return jdbcTemplate.query("SELECT * FROM mule.application WHERE muleServerId = ?",
                new MuleApplicationMapper(), muleServerId);
    }

    @Override
    public boolean applicationExists(final String applicationName, final int muleServerId) {
        List<MuleApplication> result = jdbcTemplate.query("SELECT * FROM mule.application WHERE muleServerId = ? "
                + "AND name = ?",
                new MuleApplicationMapper(), new Object[]{muleServerId, applicationName});
        return !result.isEmpty();
    }

    @Override
    public void updateMuleApplication(final MuleApplication muleApplication) {
        jdbcTemplate.update("UPDATE  mule.application SET name = ?, muleServerId = ?"
                + "WHERE id = ?", mapMuleApplicationForUpdate(muleApplication));
    }

    private Object[] mapMuleApplicationForUpdate(final MuleApplication muleApplication) {
        Object[] temp = mapMuleApplicationForInsert(muleApplication);
        Object[] result = new DaoUtils().plusOne(temp, muleApplication.getId());
        return result;
    }

    @Override
    public void removeMuleApplication(final int muleApplicationId) {
        jdbcTemplate.update("DELETE FROM mule.application WHERE id=?",
                muleApplicationId);
    }

    @Override
    public List<String> getMuleApplications() {
        List<MuleApplication> muleApplications = jdbcTemplate.query(
                "SELECT DISTINCT(name) FROM mule.application", new MuleNameMapper());
        List<String> result = new ArrayList<>();
        for (MuleApplication muleApplication : muleApplications) {
            result.add(muleApplication.getName());
        }

        return result;
    }

    @Override
    public void removeMuleApplication(final String applicationName) {
        jdbcTemplate.update("DELETE FROM mule.application WHERE name=?",
                applicationName);
    }

    private class MuleApplicationMapper implements RowMapper<MuleApplication> {
        @Override
        public MuleApplication mapRow(final ResultSet resultSet, final int i) throws SQLException {
            MuleApplication application = new MuleApplication();
            application.setId(resultSet.getInt("id"));
            application.setName(resultSet.getString("name"));
            application.setMuleServerId(resultSet.getInt("muleServerId"));
            return application;
        }
    }

    private class MuleNameMapper implements RowMapper<MuleApplication> {
        @Override
        public MuleApplication mapRow(final ResultSet resultSet, final int i) throws SQLException {
            MuleApplication application = new MuleApplication();
            application.setName(resultSet.getString("name"));
            return application;
        }
    }
}
