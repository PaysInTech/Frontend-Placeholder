package com.paysintech.techsalaries.froentend

import io.kvision.*
import io.kvision.core.BsBgColor
import io.kvision.html.br
import io.kvision.html.h1
import io.kvision.html.h3
import io.kvision.html.p
import io.kvision.navbar.NavbarExpand
import io.kvision.navbar.navbar
import io.kvision.panel.root
import kotlin.random.Random

class App : Application() {
    override fun start() {
        root("root") {
            navbar("TechSalaries [PaysInTech InfoSolutions LLP]") {
                bgColor = BsBgColor.PRIMARY
                expand = NavbarExpand.XXL
            }
            br()
            h1("We're under development")
            h3("Please do check back shortly")
            if(Random(10).nextBoolean()) {
                p("We can rise by lifting others")
            } else {
                p("By Techies for Techies")
            }
        }
    }
}

fun main() {
    startApplication(
        ::App,
        module.hot,
        BootstrapModule,
        BootstrapSpinnerModule,
        BootstrapTypeaheadModule,
        CoreModule
    )
}
