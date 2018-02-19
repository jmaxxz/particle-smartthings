/**
*  Particle Tinker
*
*  Copyright 2018 Jmaxxz
*
* MIT License
* Copyright (c) 2018 [Jmaxxz](jmaxxz.com)
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/
metadata {
    definition (name: "Particle Tinker", namespace: "particle", author: "jmaxxz") {
        capability "Actuator"
        capability "Switch"
        command "invoke", ["string", "string"]
        command "handleStateUpdate", ["json_object"]
    }


    simulator {
        // TODO: define status and reply messages here
    }

    tiles {
        multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
            }
        }
    }
}

/*
* update = [
*  published_at: <Date>,
*  coreid: <string>,
*  event: <string>(i.e. state-update), 
*  data: <object>
* ]
*/
def handleStateUpdate(update) {
    log.debug "Got update in generic handler"
    sendEvent(name: "particleState", value: update)
}

def invoke(String functionName, String value) {
    parent.invoke(this, functionName, value)
}

def on() {
    invoke("digitalwrite", "D7 HIGH")
    sendEvent(name: "switch", value: "on")
}

def off() {
    invoke("digitalwrite", "D7 LOW")
    sendEvent(name: "switch", value: "off")
}
