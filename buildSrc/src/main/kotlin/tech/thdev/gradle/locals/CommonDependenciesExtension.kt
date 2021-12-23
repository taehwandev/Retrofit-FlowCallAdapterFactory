package tech.thdev.gradle.locals

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementations(vararg elements: String) {
    elements.forEach { dependencies ->
        "implementation"(dependencies)
    }
}

fun DependencyHandlerScope.apis(vararg elements: String) {
    elements.forEach { dependencies ->
        "api"(dependencies)
    }
}

fun DependencyHandlerScope.testImplementations(vararg elements: String) {
    elements.forEach { dependencies ->
        "testImplementation"(dependencies)
    }
}

fun DependencyHandlerScope.testRuntimeOnlys(vararg elements: String) {
    elements.forEach { dependencies ->
        "testRuntimeOnly"(dependencies)
    }
}