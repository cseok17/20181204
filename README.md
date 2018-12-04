# 20181204
로그인, 장소검색, 지도보기, 인기검색어 검색

---------------------------------------------
Program
---------------------------------------------
--eclipse
	https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2018-09/R/eclipse-jee-2018-09-win32-x86_64.zip&mirror_id=1248

--apache-tomcat
	https://tomcat.apache.org/download-90.cgi
	(64-bit Windows zip --> apache-tomcat-9.0.13-windows-x64.zip)

--sqlite
	https://www.sqlite.org/download.html
	(Precompiled Binaries for Windows --> sqlite-dll-win64-x64-3250300.zip)

--sqlitebrowser (Sqlite 조회용 프로그램)
	http://sqlitebrowser.org/
	
---------------------------------------------
pom
---------------------------------------------
    <java-version>1.8</java-version>
		<org.springframework-version>4.3.2.RELEASE</org.springframework-version>

    <!-- Database (sqlite) --> 
		<dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.16.1</version>
		</dependency>
		
		<!-- JPA Provider (Hibernate) --> 
		<dependency> 
			<groupId>org.hibernate</groupId> 
			<artifactId>hibernate-entitymanager</artifactId> 
			<version>4.3.10.Final</version> 
		</dependency> 
		
		<!-- Spring Data JPA --> 
		<dependency> 
			<groupId>org.springframework.data</groupId> 
			<artifactId>spring-data-jpa</artifactId> 
			<version>1.8.1.RELEASE</version> 
		</dependency>
		
		<!-- JSON -->
		<dependency>
		    <groupId>org.json</groupId>
		    <artifactId>json</artifactId>
		    <version>20180813</version>
		</dependency>
		
		<!-- gson, json -->
		<dependency>
			<groupId>com.google.code.gson</groupId>
			<artifactId>gson</artifactId>
			<version>2.8.5</version>
		</dependency>
		
		<!-- JSON Simple -->
		<dependency>
		    <groupId>com.googlecode.json-simple</groupId>
		    <artifactId>json-simple</artifactId>
		    <version>1.1.1</version>
		</dependency>

---------------------------------------------
sqlite
---------------------------------------------
path : c:\sqlite
DBname : test.db

---------------------------------------------
실행가이드
---------------------------------------------

1. 위의 환경 설정 

2. 테이블 생성
	(1) test/src/main/resources/META-INF/persistence.xml 의 아래부분 주석 제거(tomcat으로 서비스 기동 시 다시 주석처리)
		<!-- DB create 처음 실행에만 사용 
			<property name="hibernate.hbm2ddl.auto" value="create"/>  
		-->
	(2) /test/src/main/java/com/kcs/test/db/App.java 을 Run As Java Aplication 실행

3. 테이블 생성 확인 후 (아래 프로그램으로 확인 가능)
	--sqlitebrowser
	http://sqlitebrowser.org

4. test/src/main/resources/META-INF/persistence.xml 의 아래부분 주석 처리
	<property name="hibernate.hbm2ddl.auto" value="create"/>  
		
5. Server 기동 후 아래 URL로 실행하시면 됩니다.
	(1) 로그인 
		http://localhost:8080/test/login
		(어플리케이션 실행 시 test/test로 기본 유저를 생성합니다.)
	
	(2) 장소검색 --> 로그인 완료 시 또는 아래 URL
		http://localhost:8080/test/login
	
	(3) 검색된 장소의 상세조회 --> '키워드 검색' 후 지도보기 클릭 시
	
	(4) 인기 검색어 목록  --> 장소검색 화면의 '인기검색어 보기' 클릭 시

		
