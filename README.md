# particle-smartthings
Integrate anything with smartthings using [particle.io](https://www.particle.io/). If you have smartthings and are a Maker, tinker, or hacker you likely have been frustrated with trying to get your embedded projects into smartthings. This repo is my solution to that frustration.

## How to use this repo
For the sake of brevity I will assume you know how to install smartapps and device handles from github. At a minimum you will need to install the Particle Monitor smartapp (`path to smart app here`) and the Generic Particle device handler (`path to device handler here`).

NOTE: I strongly recommend you also install webcore. This will let you interact with particles which are using the generic device handler.

## How it works
The particle monitor smart app connects to your particle account and lets you select which of your particle devices you would like to expose to SmartThings. The particle monitor smart app will create a smartthings device for each selected particle. These devices are notified of `state-update` events, can get the value of any particle variable, and can invoke funtions on the particle. In order to get the most out of your particle devices it is recommended that you create a custom device handler. However, if you are just looking for something quick and dirty webcore can be used to drive the generic device handler.

## Particle sketch changes
Exposes a string variable titled `devhandler` containing the name of your custom device handler.
Publish a `state-update` event to push state changes up to smartthings.


## Example Integrations
This repo contains several example device handlers and particle sketches which demonstrate how this integration works.
