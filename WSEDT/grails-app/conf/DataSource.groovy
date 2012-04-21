dataSource {
	pooled = false
	driverClassName = "com.mysql.jdbc.Driver"
	username = "root"
	password = ""
	url = "jdbc:mysql://localhost:3306/iawsdb?autoreconnect=true"
	}
hibernate {
  cache.use_second_level_cache=true
  cache.use_query_cache=true
  cache.provider_class='org.hibernate.cache.EhCacheProvider'
}
// environment specific settings
environments {
  development {
    dataSource {
      dbCreate = "create-drop" // one of 'create', 'create-drop','update'
      //url = "jdbc:h2:mem:devDB"
	  url = "jdbc:mysql://localhost:3306/iawsdb?autoreconnect=true"
    }
  }
  test {
    dataSource {
      dbCreate = "update"
      //url = "jdbc:h2:mem:testDb"
	  url = "jdbc:mysql://localhost:3306/iawsdb?autoreconnect=true"
    }
  }
  production {
    dataSource {
      dbCreate = "update"
      //url = "jdbc:h2:file:prodDb;shutdown=true"
	  url = "jdbc:mysql://localhost:3306/iawsdb?autoreconnect=true"
    }
  }
}