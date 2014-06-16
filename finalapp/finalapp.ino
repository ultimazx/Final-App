#include <Wire.h>
#include <Adafruit_NFCShield_I2C.h>
#include <EEPROM.h>
#include <SD.h>
#define IRQ   (2)
#define RESET (3)
Adafruit_NFCShield_I2C nfc(IRQ, RESET);
int led = 13;
// the setup routine runs once when you press reset:
void setup() {  
  Serial.begin(9600);  
  nfc.begin();
    uint32_t versiondata = nfc.getFirmwareVersion();
  if (! versiondata) {
    Serial.print("Didn't find PN53x board");
    while (1); // halt  
    }
  //board is found, begin processing
  Serial.print("Found chip PN5"); Serial.println((versiondata>>24) & 0xFF, HEX); 
  Serial.print("Firmware ver. "); Serial.print((versiondata>>16) & 0xFF, DEC); 
  Serial.print('.'); Serial.println((versiondata>>8) & 0xFF, DEC);
  // configure board to read RFID tags
  nfc.SAMConfig();
  
  Serial.println("Waiting for Card ...");
  // initialize the digital pin as an output.
  pinMode(led, OUTPUT); 
Serial.println('a');
  char a = 'b';
  while(a != 'a'){
  a = Serial.read();
  }
}

void loop(){
  uint8_t success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };  // Buffer to store the returned UID
  uint8_t uidLength;                        // Length of the UID (4 or 7 bytes depending on ISO14443A card type)
  String nameOfFile = "";
  String password = "";
  String newData = "";
if(Serial.available() > 0){
  char control = Serial.read();
  if(control == '1'){
    while(Serial.read() != NULL){
    nameOfFile += Serial.read();
  }}
  else if(control == '2'){
    while(Serial.read() != NULL){
    password += Serial.read();
    }
    if(password == "6280"){
    success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);
    delay(1000);
  if (success) {
    if((int(uid[0]) == 109) || (int(uid[0]) == 219)){
     int x = 4;
     Serial.println(x);
  }else{
    Serial.println("Invalid Security Card");
    }
  }else{
   Serial.println("Security Card Not Read");
  }
  }
  else{
  Serial.println("Invaild password");
  }
  }else if (control == '3'){
    while(Serial.read() != NULL){
        password += Serial.read();
    }
    if(password == "6280"){
     success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);
    delay(1000);
  if (success) {
    if(int(uid[0]) == 109){
        int x = 5;
        Serial.println(x);
  }
    else{
    Serial.println("Invalid Security Card");
    }
  }else{
    Serial.println("Security Card Not Read");
  }
    }else{
    Serial.println("Invalid password");
    }
  }else{
    Serial.println("Invalid Command Code");
  }
}else{
  Serial.println("Error In Serial Connection");
}

}
