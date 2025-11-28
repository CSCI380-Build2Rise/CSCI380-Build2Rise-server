package com.build2rise.backend
import com.build2rise.backend.security.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class Build2riseBackendApplication{
	@Bean
	fun testDatabaseConnection(dataSource: DataSource) = CommandLineRunner {
		println("\n" + "=".repeat(60))
		println("🔌 Testing Supabase Database Connection...")
		println("=".repeat(60))

		try {
			dataSource.connection.use { connection ->
				println("✅ Database connected successfully!")
				println("📊 Database: ${connection.metaData.databaseProductName}")
				println("📌 Version: ${connection.metaData.databaseProductVersion}")

				// Test query - count tables
				val statement = connection.createStatement()
				val resultSet = statement.executeQuery(
					"SELECT COUNT(*) as table_count FROM information_schema.tables WHERE table_schema = 'public'"
				)

				if (resultSet.next()) {
					val tableCount = resultSet.getInt("table_count")
					println("📋 Tables in database: $tableCount")

					if (tableCount >= 11) {
						println("✅ All expected tables found!")
					} else {
						println("⚠️  Expected 11 tables, found $tableCount")
					}
				}

				println("=".repeat(60) + "\n")
			}
		} catch (e: Exception) {
			println("❌ Database connection FAILED!")
			println("Error: ${e.message}")
			println("\nPlease check:")
			println("- Database URL is correct")
			println("- Password is correct")
			println("- Supabase project is running")
			println("=".repeat(60) + "\n")
		}
	}
}


fun main(args: Array<String>) {
	runApplication<Build2riseBackendApplication>(*args)
}
