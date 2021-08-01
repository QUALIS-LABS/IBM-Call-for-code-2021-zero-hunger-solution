from flask import Flask, request
from data_handler import *
from datetime import datetime
app = Flask(__name__)

response = ""




@app.route('/' , methods = ['POST', 'GET'])
def ussd_callback():
    global response
    session_id = request.values.get("sessionId" , None)
    phone_number = request.values.get("phoneNumber", None)
    text = (request.values.get("text", "default"))
    password = get_pin(phone_number)
    produce = ""
    quantity = 0
    pick_up_date = ""
    pick_up_time = ""



    level = text.split('*')

    if text == '' and  len(level) == 1:
        response = f"CON Please input the password for Phone No {phone_number}"

    elif text != '' and   len(level) == 1:
        if level[0] != password:
            response= "END You have entered an invalid pin for the user."
            response += "Please try again"
        else:
            level[0]  = "OK" 
            response = "CON Successfully logged in.\n"
            response += "Are you a farmer or trader;\n"
            response += "1. Farmer\n"
            response += "2. Trader"
    
    elif len(level) == 2 and level[1] == "1":
        response = "CON Please input the type of produce you have:\n"
        response += "1. Maize\n"
        response +="2. Beans\n"
        response += "3. Rice"
    elif len(level) == 3 and level[1] == "1":
        produce = level[2]
        response = "CON Please input the quantity(in Kgs) that you have:"
    elif len(level) == 4 and level[1] == "1":
        quantity = level[3]
        response = "CON When would you like to schedule a pickup:\n"
        response +="Please input your date in this format: 'DD-MM-YYYY'"
    elif len(level) == 5 and level[1] == "1":
        pick_up_date = level[4]
        response = "CON At what time would you like to schedule the pick-up:\n"
        response +="Please input the time in this format: 'HH:MM'"
    elif len(level) == 6 and level[1] == "1":
        pick_up_time = level[5]
        response = "CON Please input your location:\n"
    elif len(level) == 7 and level[1] == "1":
        location = level[6] 
        response ="CON Confirmation message:\n " 
        response +=f"Produce :{produce}\nQuantity(Kgs) :{quantity}Date :{pick_up_date}Time :{pick_up_time}"
        response += f"Location :{location}\n" 
        response += "1. Accept\n"
        response += "2. Decline"

    elif len(level) == 8 and level[1] == "1" and level[7] == "1":
        time = datetime.now()
        time = time.strftime('%c')
        response =f"END Your request has been received at {time} ."
        response += "You will receive an SMS with the collection details shortly."
        response += "Thank you for using Mashinani"
    elif len(level) == 8 and level[1] == "1" and level[7] == "2":
        response ="END Your response has not been recorded. Please try again."
    
    elif len(level) == 2 and level[1] == "2":
        response = "CON Please input the type of produce you want:\n"
        response += "1. Maize\n"
        response +="2. Beans\n"
        response += "3. Rice"

    elif len(level) == 3 and level[1] == "2":
        produce = level[2]
        response = "CON Please input the quantity(in Kgs) that you want:"
    elif len(level) == 4 and level[1] == "2":
        quantity = level[3]
        response = "CON When would you like to schedule a pickup:\n"
        response +="Please input your date in this format: 'DD-MM-YYYY'"
    elif len(level) == 5 and level[1] == "2":
        pick_up_date = level[4]
        response = "CON At what time would you like to schedule the delivery:\n"
        response +="Please input the time in this format: 'HH:MM'"
    elif len(level) == 6 and level[1] == "2":
        pick_up_time = level[5]
        response = "CON Please input your location:\n"
    elif len(level) == 7 and level[1] == "2":
        location = level[6] 
        response ="CON Confirmation message:\n " 
        response +=f"Produce :{produce}\nQuantity(Kgs) :{quantity}Date :{pick_up_date}Time :{pick_up_time}"
        response += f"Location :{location}\n" 
        response += "1. Accept\n"
        response += "2. Decline"

    elif len(level) == 8 and level[1] == "2" and level[7] == "1":
        time = datetime.now()
        time = time.strftime('%c')
        response =f"END Your request has been received at {time} ."
        response += "You will receive an SMS with the collection details shortly."
        response += "Thank you for using Mashinani"
    elif len(level) == 8 and level[1] == "2" and level[7] == "2":
        response ="END Your response has not been recorded. Please try again."

    
    makerequest(level[1], phone_number, produce,quantity,location,pick_up_date,pick_up_time)    
    return response
