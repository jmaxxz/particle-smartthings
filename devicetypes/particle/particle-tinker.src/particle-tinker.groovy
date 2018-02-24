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
        command "invoke", ["string", "string"]
        command "handleStateUpdate", ["json_object"]
    }


    simulator {
        // TODO: define status and reply messages here
    }

    tiles {
        valueTile("name", "", width: 3, height:1) {
            state "default", label: "Digital Write"
        }
        childDeviceTile("d0Name", "D0", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d0", "D0", height: 1, width: 1, childTileName: "switch")

        childDeviceTile("d1Name", "D1", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d1", "D1", height: 1, width: 1, childTileName: "switch")

        childDeviceTile("d2Name", "D2", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d2", "D2", height: 1, width: 1, childTileName: "switch")

        childDeviceTile("d3Name", "D3", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d3", "D3", height: 1, width: 1, childTileName: "switch")

        childDeviceTile("d4Name", "D4", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d4", "D4", height: 1, width: 1, childTileName: "switch")

        childDeviceTile("d5Name", "D5", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d5", "D5", height: 1, width: 1, childTileName: "switch")

        childDeviceTile("d6Name", "D6", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d6", "D6", height: 1, width: 1, childTileName: "switch")

        childDeviceTile("d7Name", "D7", height: 1, width: 2, childTileName: "name")
        childDeviceTile("d7", "D7", height: 1, width: 1, childTileName: "switch")
        
        
        valueTile("analogwrite_section", "", width: 3, height:1) {
            state "default", label: "Analog Write"
        }         
        childDeviceTile("a3Name", "A3", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a3", "A3", height: 1, width: 1, childTileName: "analogWrite")
        
        childDeviceTile("a6Name", "A6", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a6", "A6", height: 1, width: 1, childTileName: "analogWrite")


        valueTile("analogread_section", "", width: 3, height:1) {
            state "default", label: "Analog Read"
        }     
        childDeviceTile("a0Name", "A0", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a0", "A0", height: 1, width: 1, childTileName: "read")

        childDeviceTile("a1Name", "A1", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a1", "A1", height: 1, width: 1, childTileName: "read")

        childDeviceTile("a2Name", "A2", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a2", "A2", height: 1, width: 1, childTileName: "read")

        childDeviceTile("a4Name", "A4", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a4", "A4", height: 1, width: 1, childTileName: "read")

        childDeviceTile("a5Name", "A5", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a5", "A5", height: 1, width: 1, childTileName: "read")

        childDeviceTile("a7Name", "A7", height: 1, width: 2, childTileName: "name")
        childDeviceTile("a7", "A7", height: 1, width: 1, childTileName: "read")
    }
}

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
    addChild("D0")
    addChild("D1")
    addChild("D2")
    addChild("D3")
    addChild("D4")
    addChild("D5")
    addChild("D6")
    addChild("D7")
    addChild("A0")
    addChild("A1")
    addChild("A2")
    addChild("A3")
    addChild("A4")
    addChild("A5")
    addChild("A6")
    addChild("A7")
}

def addChild(id){
    def children = getChildDevices()
    log.debug "Looking for ${device.deviceNetworkId}.${id}"
    def myDevice = children.find({c->return c.deviceNetworkId == "${device.deviceNetworkId}.${id}"})
    if(!myDevice){
        addChildDevice("Particle Tinker Pin", "${device.deviceNetworkId}.${id}", null, [label: "${device.displayName} ${id}", completedSetup:true, isComponent: true, name:"${id}", componentName: "${id}", componentLabel: "Pin ${id}"])
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
    def result =  parent.invoke(this, functionName, value)
    log.debug "invoked ${functionName} got ${result}"
    return result
}


def invokeForChild(childDevice, String func = "", String msg= "") {
    return invoke(func, msg);
}