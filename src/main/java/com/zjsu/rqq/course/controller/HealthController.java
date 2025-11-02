package com.zjsu.rqq.course.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
class HealthController {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health/db")
    public Map<String, Object> checkDb() {
        Map<String, Object> health = new HashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            // 测试基本连接
            health.put("database", "✓ 连接正常");
            health.put("product", conn.getMetaData().getDatabaseProductName());
            health.put("version", conn.getMetaData().getDatabaseProductVersion());

            // 测试简单查询
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            health.put("queryTest", result == 1 ? "✓ 查询正常" : "✗ 查询异常");

            // 检查表是否存在
            try {
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students", Integer.class);
                health.put("tables", "✓ 表结构正常");
            } catch (Exception e) {
                health.put("tables", "⚠ 表结构可能不完整");
            }

        } catch (Exception e) {
            health.put("database", "✗ 连接失败: " + e.getMessage());
            health.put("queryTest", "✗ 无法执行查询");
            health.put("tables", "✗ 无法检查表结构");
        }

        return health;
    }
}
