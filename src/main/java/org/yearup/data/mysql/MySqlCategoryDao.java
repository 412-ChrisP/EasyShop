package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error fetching all categories", e);
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        Category category = null;
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                category = mapRow(resultSet);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error fetching category by ID", e);
        }

        return category;
    }

    @Override
    public Category create(Category category)
    {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0)
            {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next())
                {
                    category.setCategoryId(generatedKeys.getInt(1));
                }
            }
            else
            {
                throw new SQLException("Creating category failed, no rows affected.");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error creating category", e);
        }

        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {

    }

    @Override
    public void delete(int categoryId)
    {

    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
