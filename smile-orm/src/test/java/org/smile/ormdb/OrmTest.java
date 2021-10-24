package org.smile.ormdb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.smile.beans.converter.BeanException;
import org.smile.collection.CollectionUtils;
import org.smile.collection.MapUtils;
import org.smile.dataset.DataSet;
import org.smile.db.DbManager;
import org.smile.db.PageHelper;
import org.smile.db.PageParam;
import org.smile.db.config.ResourceConfig;
import org.smile.db.criteria.MatchMode;
import org.smile.db.handler.ResultSetMap;
import org.smile.db.jdbc.LambdaRecordDao;
import org.smile.db.jdbc.RecordDao;
import org.smile.db.pool.BasicDataSource;
import org.smile.db.sql.parameter.BatchParameterMap;
import org.smile.expression.Expression;
import org.smile.ioc.ClassPathIocContext;
import org.smile.json.JSON;
import org.smile.json.JSONObject;
import org.smile.json.JSONValue;
import org.smile.lambda.Lambda;
import org.smile.orm.ioc.DaoSupport;
import org.smile.orm.parser.OrmWhereSqlParser;
import org.smile.orm.parser.ParseException;
import org.smile.orm.record.RecordDaoImpl;
import org.smile.ormdb.dao.ITestDao;
import org.smile.ormdb.dao.ITestRecordDao;
import org.smile.ormdb.dao.Student;
import org.smile.ormdb.dao.UpdateInfo;
import org.smile.template.SimpleStringTemplate;

import junit.framework.TestCase;
import org.smile.util.SysUtils;
import org.springframework.core.env.SystemEnvironmentPropertySource;

public class OrmTest {
    ITestDao dao;
    DaoSupport daoSupport;
    RecordDao<Student> studentDao;
    IStudentService studentService;
    ClassPathIocContext context;
    LambdaRecordDao<Student> lambdaRecordDao;

    @Before
    public void setUp() throws BeanException, SQLException, FileNotFoundException {
        ResourceConfig config = new ResourceConfig();
        config.setUrl("jdbc:mysql://localhost:3306/mytest");
        config.setUsername("root");
        config.setPassword("password");
        config.setDriver("com.mysql.jdbc.Driver");
        DataSource ds = new BasicDataSource(config);
        DbManager.registDataSource("mysql", ds, true);
        String path = new SimpleStringTemplate("${user.dir}\\src\\test\\java\\ioc.xml").processToString(System.getProperties());
        context = new ClassPathIocContext(new FileInputStream(path));
        dao = context.getBean(ITestDao.class);
        daoSupport = context.getBean(DaoSupport.class);
        studentDao = new RecordDaoImpl<Student>(Student.class);
        studentService = context.getBean(IStudentService.class);
        lambdaRecordDao = studentDao;
    }
    @Test
    public void testInsert(){
        Student s = new Student();
        s.setName("胡真山");
        s.setAddress("湖南长沙");
        s.setAge(109);
        s.insert();
    }

    @Test
    public void testOrm() throws SQLException {
        Map params = CollectionUtils.hashMap("name", "胡");
        params.put("age", 10);
        PageHelper.startPage(new PageParam(1, 10));
        Student s = new Student();
        s.setId(22L);
        dao.load(s);
        System.out.println(JSONObject.toJSONString(s));
        UpdateInfo info = new UpdateInfo();
        info.setUpdateUser("胡真山");
        info.setCreateUser("胡真山");
        info.setUpdateTime(new Date());
        s.setUpdateInfo(info);
        s.save();

        TestCase.assertEquals(s.getId(), s.getId());
        PageHelper.remove();
        DataSet dataset = dao.queryDatas(s);
        System.out.println(dataset.rowCount());
    }

    @Test
    public void testWhereParser() throws SQLException {
        List<Student> stu = daoSupport.query(Student.class, "firstName = ? and age >? order by firstName", "胡", 10);
        System.out.println(JSON.toJSONString(stu));
        List<Student> list = Student.dao.query("firstName = '胡' and age >10");
        System.out.println(list);
        if(list.size()>0) {
            list.get(0).setName("小白啊");
            list.get(0).save();
            list.get(0).update(new String[]{"name"});
        }
    }

    @Test
    public void testNamedWhere() throws SQLException {
        Map params = new HashMap();
        params.put("firstName", "胡");
        params.put("age", 10);
        params.put("name", CollectionUtils.arrayList("胡真山", "小白"));
        List<Student> stu = daoSupport.query(Student.class, "firstName = #firstName and age >#age and (firstName+'10')>'111' and name in (:name) order by firstName", params);
        System.out.println(JSON.toJSONString(stu));
        List<Student> list = Student.dao.query("firstName = '胡' and age >10");
        System.out.println(list);
        if(list.size()>0) {
            list.get(0).setName("小白啊");
            list.get(0).save();
            list.get(0).update(new String[]{"name"});
        }
    }

    @Test
    public void testCriteria() throws SQLException {
        List<Student> s = studentDao.lambda().like(Student::getName, "%胡%").eq(Student::getAge, 1000).list();
        System.out.println(s);
        s = studentDao.criteria().like("firstName", "真", MatchMode.END).top(10);
        System.out.println(s);
        List<Student> stus = studentDao.criteria().notnull("email").top(10);
        JSONValue.printToConsle(stus);
        List<Student> stus2 = studentDao.criteria().notnull("firstName").top(10);
        JSONValue.printToConsle(stus2);
        JSONValue.printToConsle(studentDao.criteria().field("firstName").offset(3).limit(5).listMap());
    }

    @Test
    public void testParserWhereExp() throws ParseException {
        Expression exp = OrmWhereSqlParser.parse("(firstName + '10')>'111'");
        System.out.println(exp);
    }

    @Test
    public void testDao() {
        List list = dao.queryStudentAsMap(CollectionUtils.hashMap("name", "%白%"));
        System.out.println(list);
    }

    @Test
    public void testRecrodDao() throws BeanException, SQLException {
        ITestRecordDao recordDao = context.getBean(ITestRecordDao.class);
        List<Student> students = recordDao.criteria().limit(2).list();
        if(students.size()>0){
            students.get(0).load();
        }
        List<ResultSetMap> maps = recordDao.criteria().field("updateUser").field("updateTime").fields("name", "age").listMap();
        System.out.println(maps.size());
        System.out.println(maps);
		students=recordDao.queryStudent(MapUtils.hashMap("name", "白"));
    }

    @Test
    public void testBatch() {
        List list = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Map m = new HashMap<>();
            m.put("name", "胡真山" + i);
            m.put("id", i);
            list.add(m);
        }
        int c = dao.batchUpdate(list);
        System.out.println(c);
    }

    @Test
    public void testBatchDelete() {
        Set list = CollectionUtils.hashSet(1, 2, 3, 4);
        dao.deleteByIds(list.toArray());
    }

    @Test
    public void testBatchDelete2() {
        Set list = CollectionUtils.hashSet(1, 2, 3, 4);
        BatchParameterMap map = new BatchParameterMap(list);
        map.put("ids", list);
        map.put("name", "胡");
        dao.deleteBatchByIds(map);
    }

    @Test
    public void testTransaction() {
        this.studentService.updateStudent();
    }

    @Test
    public void testLambda() {
        Student s = new Student();
        s.setId(20L);
        s.setName("中国好学生");
        s.setAddress("中国好学生之乡");

        this.lambdaRecordDao.update(s, Student::getName);
        this.lambdaRecordDao.update(s,Student::getName,Student::getAge,Student::getAddress);
        List students=this.lambdaRecordDao.criteria().in(Student::getId,CollectionUtils.linkedList(20,21)).field(Student::getName).field(Student::getId).listMap();
        SysUtils.println(students);
        students= this.studentDao.query("id in (?,?,?)",10,20,30);
        SysUtils.log(JSON.toJSONString(students));
    }
    @Test
    public void testLambdaUpdate(){
        this.lambdaRecordDao.lambda().set(Student::getName,"真是个小白吃啊").set(Student::getAge,10000).eq(Student::getId,22).update();
        int i=this.lambdaRecordDao.lambda().set(Student::getName,"真是个小白吃啊").set(Student::getAge,10).le(Student::getAge,10).update();
        SysUtils.log(i);
    }
}
