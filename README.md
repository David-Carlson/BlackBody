# BlackBody Radiation Simulator
I simulated blackbody radiation, which is light (and other spectrums) given off an object due to heat. [Wikipedia](https://en.wikipedia.org/wiki/Black-body_radiation) elaborates well on this. 
I also created an offshoot of this in ProcessingJS.  [Check it out, it's interactive!](khan)  

For a given heat, I can calculate the power of any particular wavelength of electro-magnetic radiation. 
This can be done through Plank's law of black-body radiation:
![Wavelength vs power graph](powerGraph)  

## Synopsis

At the top of the file there should be a short introduction and/ or overview that explains **what** the project is. This description should match descriptions added for package managers (Gemspec, package.json, etc.)

## Code Example

Show what the library does as concisely as possible, developers should be able to figure out **how** your project solves their problem by looking at the code example. Make sure the API you are showing off is obvious, and that your code is short and concise.

## Motivation

A short description of the motivation behind the creation and maintenance of the project. This should explain **why** the project exists.

## Installation

Provide code examples and explanations of how to get the project.

## API Reference

I used the [Sunflow Render Engine](http://sunflow.sourceforge.net/docs/javadoc/) to help draw images as well as handle parts of the spectrum conversion.


## Contributors

Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

[wavelength graph]: /images/wavelengthGraph.svg
[plank]: /images/planck.svg
[khan]: https://www.khanacademy.org/computer-programming/black-body-radiation-simulator/4530109545316352 "ProcessingJS version of program"
[powerGraph]: https://qph.ec.quoracdn.net/main-qimg-396f093ef6c69e0110dd9b149eaa9b6c?convert_to_webp=true
