package Homework5.Util;


import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

@UtilityClass
public class ConnectDB {

    private static String resource = "mybatis-config.xml";

    private static SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory;
        //sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resource));
        sqlSessionFactory= new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
        return sqlSessionFactory.openSession(true);
    }

    @SneakyThrows
    public static db.dao.CategoriesMapper getCategoriesMapper(){
        return getSqlSession().getMapper(db.dao.CategoriesMapper.class);
    }
    @SneakyThrows
    public static db.dao.ProductsMapper getProductsMapper() {
        return getSqlSession().getMapper(db.dao.ProductsMapper.class);
    }

    public static Integer countCategories(db.dao.CategoriesMapper categoriesMapper) {
        long categoriesCount = categoriesMapper.countByExample(new db.model.CategoriesExample());
        return Math.toIntExact(categoriesCount);
    }

    public static Integer countProducts(db.dao.ProductsMapper productsMapper) {
        long products = productsMapper.countByExample(new db.model.ProductsExample());
        return Math.toIntExact(products);
    }

    public static db.model.Products selectProductById(db.dao.ProductsMapper productsMapper, long key){
        return productsMapper.selectByPrimaryKey(key);
    }
}

