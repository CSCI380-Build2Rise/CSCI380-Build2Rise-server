package com.build2rise.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Build2riseBackendApplication

fun main(args: Array<String>) {
	runApplication<Build2riseBackendApplication>(*args)
}
