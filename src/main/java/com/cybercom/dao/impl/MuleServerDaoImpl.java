package com.cybercom.dao.impl;

import com.cybercom.DaoUtils;
import com.cybercom.dao.MuleServerDao;
import com.cybercom.dao.objects.MuleServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 10:57 AM
 */
public class MuleServerDaoImpl implements MuleServerDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(MuleServerDaoImpl.class);

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createMuleServer(final MuleServer muleServer) {
        jdbcTemplate.update("INSERT INTO mule.server(name, serverAddress, prefix)"
                + "VALUES(?, ?, ?)", mapMuleServerForInsert(muleServer));
    }

    private Object[] mapMuleServerForInsert(final MuleServer muleServer) {
        Object[] result = new Object[3];
        int i = 0;
        result[i++] = muleServer.getName();
        result[i++] = muleServer.getServerAddress();
        result[i] = muleServer.getPrefix();
        return result;
    }

    @Override
    public MuleServer getMuleServer(final int id) {
        final List<MuleServer> muleServerList = jdbcTemplate.query("SELECT * FROM mule.server WHERE id = ?",
                new MuleServerMapper(), new Object[]{id});
        MuleServer result = null;
        if (muleServerList.size() > 0) {
            result = muleServerList.get(0);
        }
        return result;
    }

    @Override
    public MuleServer getMuleServer(final String muleServerName) {
        final List<MuleServer> muleServerList = jdbcTemplate.query("SELECT * FROM mule.server WHERE name = ?",
                new MuleServerMapper(), new Object[]{muleServerName});
        MuleServer result = null;
        if (!muleServerList.isEmpty()) {
            result = muleServerList.get(0);
        }
        return result;
    }

    @Override
    public void updateMuleServer(final MuleServer muleServer) {
        jdbcTemplate.update("UPDATE mule.server SET name = ?, serverAddress = ?, prefix = ?"
                + "WHERE id=?", mapMuleServerForUpdate(muleServer));
    }

    @Override
    public List<MuleServer> getAllMuleServers() {
        return jdbcTemplate.query("SELECT * FROM mule.server",
                new MuleServerMapper());
    }

    private Object[] mapMuleServerForUpdate(final MuleServer muleServer) {
        Object[] temp = mapMuleServerForInsert(muleServer);
        Object[] result = new DaoUtils().plusOne(temp, muleServer.getId());
        return result;
    }

    private static final class MuleServerMapper implements RowMapper<MuleServer> {

        @Override
        public MuleServer mapRow(final ResultSet resultSet, final int i) throws SQLException {
            MuleServer server = new MuleServer();
            server.setId(resultSet.getInt("id"));
            server.setName(resultSet.getString("name"));
            server.setServerAddress(resultSet.getString("serverAddress"));
            server.setPrefix(resultSet.getString("prefix"));
            return server;
        }
    }
}
