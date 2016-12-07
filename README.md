# BlackBody Radiation Simulator
I simulated blackbody radiation, which is light (and other spectrums) given off an object due to heat. [Wikipedia](https://en.wikipedia.org/wiki/Black-body_radiation) elaborates well on this. I created this project with a friend of mine, Sean Ceallaign. 
I also created an offshoot of this in ProcessingJS.  [Check it out, it's interactive!](https://www.khanacademy.org/computer-programming/black-body-radiation-simulator/4530109545316352)  

For a given heat, I can calculate the power of any particular wavelength of electro-magnetic radiation. 
This can be done through Plank's law of black-body radiation. This creatures a spectrum graph that corresponds with a color. Finding the color in RGB terms involves converting it into the CIE X,Y,Z standard of color, which involves integration. Then I convert into RGB using linear algebra. 

## Motivation

I'm curious about light and graphics. I had previously created an [Atmospheric Scattering Simulator](https://github.com/ajzaff/sky-simulator), and wanted to learn more about applications of physics and how wavelength corresponds to color. 


## API Reference

I used the [Sunflow Render Engine](http://sunflow.sourceforge.net/docs/javadoc/) to help draw images as well as handle parts of the spectrum conversion.

## License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

[wavelength graph]: /images/wavelengthGraph.svg
[plank]: /images/planck.svg
[powerGraph]: https://qph.ec.quoracdn.net/main-qimg-396f093ef6c69e0110dd9b149eaa9b6c?convert_to_webp=true
