# Overview

This project is a simple application to retrieve weather information from a REST API (Open-Meteo). This is my first attempt at building a program from the ground up that uses an API, so this will be a valueable learning experience. 

The goal of this project is, initially, to run using hard coded values, retrieving hourly weather information for Lehi, Utah. I'll then expand the functinality to allow for user defined variables such as the location to retreive information for, and optional additional information such as wind speed, UV index, etc.


[Software Demo Video](https://youtu.be/TkiORru7E-I)

# Development Environment

This project was developed with Java 21 using the Standard Java Library, on VSCode.

# Useful Websites

- [Open-Meteo Documentation](https://open-meteo.com/en/docs)
- [Chat GPT](https://chatgpt.com/)
- [Java Documentation](https://docs.oracle.com/en%2Fjava%2Fjavase%2F11%2Fdocs%2Fapi%2F%2F/index.html)
- [How To Call a REST API In Java - Simple Tutorial](https://youtu.be/9oq7Y8n1t00?si=SdAOGBoA2VVTImP_)
- [How to get user input in Java](https://stackoverflow.com/questions/5287538/how-to-get-the-user-input-in-java)


# Future Work

- Option 1 needs to be configured to allow the user to set a location, whether through entering coordinates or typing a city name.
- I would like to reformat the timestamps in the hourly temperature readout to provide the time in the users locale, rather than GMT.
- Providing additional information, such as precipitation, would be very nice to add.