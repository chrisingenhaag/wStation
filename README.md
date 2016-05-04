# wStation

This is a [JHipster][] based web application for the [tinkerforge][] weather station. The application saves the weather stations sensor values quartz-cron based. The sensor data is accessible via d3 graph integration.

# Instructions

* Install and run brick daemon from http://www.tinkerforge.com/de/doc/Downloads.html
* check src/main/resources/config/application-dev.yml for tinker ip connection information
* run webapp with ./gradlew bootRun

[JHipster]: https://jhipster.github.io/
[tinkerforge]: http://www.tinkerforge.com/de/doc/Kits/WeatherStation/WeatherStation.html
