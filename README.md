# Timetables
An application made to view Arrivals and Departures for every RFI managed passenger railway station.<br>

It supports multiple platforms as it's a Kotlin Multiplatform Compose application but currently only 
Android APKs are available to be downloaded in the release.<br>

The application is currently going through a major rework of pretty much the entire codebase, this means 
that building the project right now will result in an incomplete experience. Furthermore, Trenitalia has 
changed the HTML of their InfoLavori section, so that will have to be reworked too.

## Thanks to
- [mrjafari-dev](https://github.com/mrjafari-dev) for the [Skeleton Loading Effect](https://github.com/mrjafari-dev/skeleton-loading-effect)

## Downloads
Right now the GitHub releases are a bit behind, but soon the app will be available on the Google Play Store.

## Features
- Search across all passenger stations
- Visualize departures and arrivals
- View more details for each train
- Save favourite stations for rapid access (will be available in next release)
- An improved Desktop and Tablet UI (versions 1.3.4 and above)
- Automatic data refresh every 5 minutes (versions 1.3.4 and above)
- Visualizer for future or current line suspensions

## Planned additions
- MacOS and Windows builds

## External links
This application uses the publicly available [RFI Monitor Arrivi&Partenze Live website]([url](https://iechub.rfi.it/ArriviPartenze/ArrivalsDepartures/Home)) and parses the data in the HTML tables to then show to the user.

## License
This app is licensed under the [GPLv3 License](COPYING)
