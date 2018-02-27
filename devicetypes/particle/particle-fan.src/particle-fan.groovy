/**
*  Particle Fan
*
*  Copyright 2018 Jmaxxz
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*
*/
metadata {
    definition (name: "Particle Fan", namespace: "particle", author: "Jmaxxz") {
        capability "Actuator"
        capability "Switch"
        capability "Fan Speed"
        command "setFanLow"
        command "setFanMedium"
        command "setFanHigh"
        command "toggleLight"
        attribute "fanAddress", "string"
    }


    simulator {
        // TODO: define status and reply messages here
    }

    tiles {
        multiAttributeTile(name:"fanSpeed", type:"generic", width:6, height:4, canChangeIcon: true) {
            tileAttribute("device.fanSpeed", key: "PRIMARY_CONTROL") {
                attributeState "0", label:'OFF', action: "setFanMedium", icon: "st.Lighting.light24", nextState: "2"
                attributeState "1", label:'LOW', action: "switch.off", icon: "st.Lighting.light24", nextState: "0", backgroundColor: "#00A0DC"
                attributeState "2", label:'MEDIUM', action: "switch.off", icon: "st.Lighting.light24", nextState: "0", backgroundColor: "#00A0DC"
                attributeState "3", label:'HIGH', action: "switch.off", icon: "st.Lighting.light24", nextState: "0", backgroundColor: "#00A0DC"
            }
            tileAttribute("fanAddress", key: "SECONDARY_CONTROL"){
                attributeState "default", label:'FAN: ${currentValue}'    
            }
        }
        standardTile("lowSpeed", "device.fanSpeed", decoration: "flat", width: 2, height: 2) {
        	state "default", label:'LOW', action: "setFanLow", nextState: "1", icon:"st.Lighting.light24"
            state "1", label:'LOW', action: "switch.off", icon:"st.Lighting.light24", nextState: "0", backgroundColor: "#00A0DC"
        }
        standardTile("medSpeed", "device.fanSpeed", decoration: "flat", width: 2, height: 2) {
        	state "default", label:'MED', action: "setFanMedium", nextState: "2", icon:"st.Lighting.light24"
            state "2", label: 'MED', action: "switch.off", icon:"st.Lighting.light24", nextState: "0", backgroundColor: "#00A0DC"
        }
        standardTile("highSpeed", "device.fanSpeed", decoration: "flat", width: 2, height: 2) {
        	state "default", label:'HIGH', action: "setFanHigh", nextState: "3", icon:"st.Lighting.light24"
            state "3", label: 'HIGH', action: "switch.off", icon:"st.Lighting.light24", nextState: "0", backgroundColor: "#00A0DC"
        }
        
        standardTile("toggleLight", "device.fanAddress", decoration: "flat", width: 2, height: 2) {
        	state "default", label:'Light', action: "toggleLight", icon:"st.Lighting.light11"
        }
    }
}

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
	refresh()
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
}

def on(){
    setFanSpeed(2)
}
def off(){
    setFanSpeed(0)
}
def setFanLow(){
    setFanSpeed(1)
}
def setFanMedium(){
    setFanSpeed(2)
}
def setFanHigh(){
    setFanSpeed(3)
}
// handle commands
def setFanSpeed(Number speed) {
    log.debug "Executing 'setFanSpeed'"
    if(speed < 1){
        parent.invokeFromChild(this, "cmd", "o${device.name}")
        sendEvent(name: "fanSpeed", value: 0)
        sendEvent(name: "switch", value: "off")
    } else if(speed <2){
        parent.invokeFromChild(this, "cmd", "l${device.name}")
        sendEvent(name: "fanSpeed", value: 1)
        sendEvent(name: "switch", value: "on")
    } else if(speed < 3){
        parent.invokeFromChild(this, "cmd", "m${device.name}")
        sendEvent(name: "fanSpeed", value: 2)
        sendEvent(name: "switch", value: "on")
    } else {
        parent.invokeFromChild(this, "cmd", "h${device.name}")
        sendEvent(name: "fanSpeed", value: 3)
        sendEvent(name: "switch", value: "on")
    }
}

def toggleLight(){
	parent.invokeFromChild(this, "cmd", "b${device.name}")
}