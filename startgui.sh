#!/bin/bash
rmiregistry &
java ParanoidGUI
killall rmiregistry
