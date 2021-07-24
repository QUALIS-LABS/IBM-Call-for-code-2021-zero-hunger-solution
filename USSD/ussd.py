from flask import Flask, request
app = Flask(__name__)

response = ""

@app.route('/' , methods = ['POST', 'GET'])
def ussd_callback():
    global response
    session_is = request.values.get("sessionId" , None)
    phone_number = request.values.get("phoneNumber", None)
    text = (request.values.get("text", "default"))

if text == '':
    response  = "CON Are you a farmer or a trader? \n"
    response += "1. Farmer \n"
    response += "2. Trader"

 elif text == '1':
    response = "CON What produce do you have? \n"
    response += "1. Maize \n"
    response += "2. Wheat"
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
    response = "CON What are you looking for "
    response += "1. Maize \n"
    response += "2. Wheat"
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

 return response
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=os.environ.get('PORT'))
