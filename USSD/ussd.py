from flask import Flask, request
import os
app = Flask(__name__)

response = ""
pin = "" #to introduce pin

@app.route('/' , methods = ['POST', 'GET'])
def ussd_callback():
    global response
    session_id = request.values.get("sessionId" , None)
    phone_number = request.values.get("phoneNumber", None)
    text = (request.values.get("text", "default"))

    if text == '':
        response  = "CON Are you a farmer or a trader? \n"
        response += "1. Farmer \n"
        response += "2. Trader"
    
    elif text == '1':
        response = "CON What produce do you have? \n"
        response += "1. Maize \n"
        response += "2. Wheat \n"
        response += "3. Beans"

    elif text == '1*1':
        phoneNumber  = "0712345689"
        response = "END Contact this phonenumber " + phoneNumber

    elif text == '1*2':
        phoneNumber  = "0712345689"
        response = "END Contact this phonenumber " + phoneNumber
        
    elif text == '1*3':
        phoneNumber  = "0712345689"
        response = "END Contact this phonenumber " + phoneNumber
        
    elif text == '2':
        response = "CON What are you looking for\n "
        response += "1. Maize \n"
        response += "2. Wheat \n"
        response += "3. Beans"
        
    elif text == '2*1':
        phoneNumber  = "0712345689"
        response = "END Contact this phonenumber " + phoneNumber

    elif text == '2*2':
        phoneNumber  = "0712345689"
        response = "END Contact this phonenumber " + phoneNumber
        
    elif text == '2*3':
        phoneNumber  = "0712345689"
        response = "END Contact this phonenumber " + phoneNumber

    return response
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=os.environ.get('PORT'))
