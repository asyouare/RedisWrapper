package org.asyou.redis.base;

import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

/**
 * Created by steven on 17/5/4.
 */
public class RedisConfig implements Cloneable,Serializable {

    public static RedisConfig build(){
        return new RedisConfig();
    }

    private String id;
    private String hostName;
    private Integer port;
    private String userName;
    private String password;


    private JedisPoolConfig jedisPoolConfig;

    public RedisConfig(){
        this.jedisPoolConfig = createJedisPoolConfig();
    }

    public RedisConfig(String id,
                       String hostName,
                       Integer port){
        init(id, hostName, port, null, null,null);
    }

    public RedisConfig(String id,
                       String hostName, Integer port,
                       String userName, String password){
        init(id, hostName, port, userName, password, null);
    }

    public RedisConfig(String id,
                       String hostName, Integer port,
                       String userName, String password,
                       JedisPoolConfig jedisPoolConfig) {
        init(id,hostName,port,userName,password,jedisPoolConfig);
    }

    protected void init(String id,
                        String hostName, Integer port,
                        String userName, String password,
                        JedisPoolConfig jedisPoolConfig){
        this.id = id;
        this.hostName = hostName;
        this.port = port;
        this.userName = userName;
        this.password = password;
        if (jedisPoolConfig == null) {
            this.jedisPoolConfig = createJedisPoolConfig();
        } else {
            this.jedisPoolConfig = jedisPoolConfig;
        }
    }

    public String getId(){
        return id;
    }

    public RedisConfig setId(String id){
        this.id = id;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public RedisConfig setHostName(String hostname) {
        this.hostName = hostname;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public RedisConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public RedisConfig setUserName(String username) {
        this.userName = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public JedisPoolConfig getJedisPoolConfig(){ return this.jedisPoolConfig; }

    public RedisConfig setJedisPoolConfig(JedisPoolConfig value){
        this.jedisPoolConfig = value;
        return this;
    }


    /**
     * 克隆对象
     * @return 返回复制品
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RedisConfig: id=" + id +
                ", hostName=" + hostName + ", port=" + port +
                ", userName=" + userName + ", password=" + password;
    }

    protected JedisPoolConfig createJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setBlockWhenExhausted(true);
        jedisPoolConfig.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        jedisPoolConfig.setJmxEnabled(true);
        jedisPoolConfig.setJmxNamePrefix("pool");
        jedisPoolConfig.setLifo(true);
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMaxTotal(500);
        jedisPoolConfig.setMaxWaitMillis(5 * 1000);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(10 * 60 * 1000);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(10 * 60 * 1000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(-1);
        return jedisPoolConfig;
    }

    /*
    <bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
        <!--连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true-->
        <property name="blockWhenExhausted" value="true"></property>
        <!-- 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数) -->
        <property name="evictionPolicyClassName" value="org.apache.commons.pool2.impl.DefaultEvictionPolicy"></property>
        <!-- 是否启用pool的jmx管理功能, 默认true -->
        <property name="jmxEnabled" value="true"></property>
        <!-- MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默 认为"pool" -->
        <property name="jmxNamePrefix" value="pool"></property>
        <!-- 是否启用后进先出, 默认true -->
        <property name="lifo" value="true"></property>
        <!-- 最大空闲连接数, 默认8个 -->
        <property name="maxIdle" value="256"></property>
        <!-- 最大连接数, 默认8个 -->
        <property name="maxTotal" value="2000"></property>
        <!-- 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1 -->
        <property name="maxWaitMillis" value="5000"></property>
        <!-- 逐出连接的最小空闲时间 默认1800000毫秒(30分钟) -->
        <property name="minEvictableIdleTimeMillis" value="2000"></property>
        <!-- 最小空闲连接数, 默认0 -->
        <property name="minIdle" value="0"></property>
        <!-- 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 -->
        <property name="NumTestsPerEvictionRun" value="3"></property>
        <!-- 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略) -->
        <property name="softMinEvictableIdleTimeMillis" value="2000"></property>
        <!-- 在获取连接的时候检查有效性, 默认false -->
        <property name="testOnBorrow" value="true"></property>
        <!-- 在空闲时检查有效性, 默认false -->
        <property name="testWhileIdle" value="true"></property>
        <property name="testOnReturn" value="true"></property>
        <!-- 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1 -->
        <property name="timeBetweenEvictionRunsMillis" value="-1"></property>
    </bean>
    */
}
