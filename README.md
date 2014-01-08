myezteam-webservices
=====================

My EZ Team webservices

## Current version: v1
```
All endpoints prefixed with version, currenlty v1
ie: GET /v1/teams
```

## Api Keys
### Each request will require an api key as a query parameter, to request an api key, please email admin@myezteam.com
####
```
example: POST /v1/auth/login?api_key=a344ba35-e9b1-4360-9335-1c200f8f8d4d
```

## Authentication
### POST /auth/login
#### Example data
```
{
  "email":"junker37@gmail.com",
  "password":"myPassw0Rd"
}
```
#### Response
```
{
    "token": "c97fede5-8316-49bb-8d0a-3eeb91fe4a02",
    "user_id": 3,
    "created": 1383662745575
}
```

## Team Resources

### GET /teams
#### Get teams a user is a player on
#### Response
```
[
    {
        "id": 3,
        "name": "Sloppy Waffles",
        "type": "broomball",
        "default_location": "Withers",
        "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
    },
    {
        "id": 21,
        "name": "Eagle Drug",
        "type": "Softball",
        "default_location": "McQuillan Field ",
        "description": ""
    }
]
```

### GET /teams/owner
#### Get teams a user owns (created)s
#### Response
```
[
    {
        "id": 3,
        "name": "Sloppy Waffles",
        "type": "broomball",
        "default_location": "Withers",
        "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
    },
    {
        "id": 21,
        "name": "Eagle Drug",
        "type": "Softball",
        "default_location": "McQuillan Field ",
        "description": ""
    }
]
```

### GET /teams/manager
#### Get teams a user manages
#### Response
```
[
    {
        "id": 3,
        "name": "Sloppy Waffles",
        "type": "broomball",
        "default_location": "Withers",
        "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
    },
    {
        "id": 21,
        "name": "Eagle Drug",
        "type": "Softball",
        "default_location": "McQuillan Field ",
        "description": ""
    }
]
```

### GET /teams/all
#### Get all teams a user plays on/owns/manages by type
#### Response
```
{
    "manager": [
        {
            "id": 173,
            "name": "Cradick Volleyball",
            "type": "Volleyball League",
            "default_location": "Cradick House",
            "description": "Open pickup volleyball games"
        }
    ],
    "player": [
        {
            "id": 3,
            "name": "Sloppy Waffles",
            "type": "broomball",
            "default_location": "Withers",
            "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
        },
        {
            "id": 21,
            "name": "Eagle Drug",
            "type": "Softball",
            "default_location": "McQuillan Field ",
            "description": ""
        }
    ],
    "owner": [
        {
            "id": 3,
            "name": "Sloppy Waffles",
            "type": "broomball",
            "default_location": "Withers",
            "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
        },
        {
            "id": 46,
            "name": "Chupacabras",
            "type": "Softball",
            "default_location": "McQuillan IV",
            "description": "Fall league softball team"
        }
    ]
}
```

### GET /teams/{team_id}
#### Get a team
#### Response
```
{
    "id": 3,
    "name": "Sloppy Waffles",
    "type": "broomball",
    "default_location": "Withers",
    "owner_id":3,
    "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
}
```

### GET /teams/{team_id}/players
#### Get a team's players
```
[
    {
        "id": 51,
        "user_id": 64,
        "team_id": 3,
        "user": {
            "id": 64,
            "email": "boecker@us.ibm.com",
            "first_name": "Doug",
            "last_name": "Boecker"
        },
        "team": null,
        "player_type": "Regular"
    },
    {
        "id": 52,
        "user_id": 5,
        "team_id": 3,
        "user": {
            "id": 5,
            "email": "cradick@gmail.com",
            "first_name": "Ryan",
            "last_name": "Cradick"
        },
        "team": null,
        "player_type": "Regular"
    },
    {
        "id": 54,
        "user_id": 66,
        "team_id": 3,
        "user": {
            "id": 66,
            "email": "bwmashak@charter.net",
            "first_name": "Ben",
            "last_name": "Mashak"
        },
        "team": null,
        "player_type": "Member"
    }
]
```

### POST /teams
#### Create a team
#### Example data
```
{
  "name": "My New Team",
  "type": "Broomball",
  "default_location": "Graham Arena",
  "description": "Pickup broomball games"
}
```
#### Response
```
{
    "id": 181,
    "owner_id": 3,
    "name": "My New Team",
    "type": "Broomball",
    "default_location": "Graham Arena",
    "description": "Pickup broomball games"
}
```

### PUT /teams
#### Update a team
#### Example data
```
{
    "id": 3,
    "name": "Sloppy Waffles",
    "type": "broomball",
    "default_location": "Withers",
    "description": "Outdoor broomball team"
}
```

### DELETE /teams/{team_id}
#### Delete a team, user must be the owner


### GET /teams/{team_id}/owner
#### Get a team's owner
```
{
    "id": 3,
    "email": "junker37@gmail.com",
    "first_name": "Jeremy",
    "last_name": "McJunkin"
}
```

### GET /teams/{team_id}/managers
#### Get a team's managers
```
[
    {
        "id": 64,
        "email": "boecker@us.ibm.com",
        "first_name": "Doug",
        "last_name": "Boecker"
    },
    {
        "id": 105,
        "email": "michael.burman@gmail.com",
        "first_name": "Mike",
        "last_name": "Burman"
    },
    {
        "id": 129,
        "email": "nathan.rabe@gmail.com",
        "first_name": "Nate",
        "last_name": "Rabe"
    }
]
```

### POST /teams/{team_id}/managers
#### Add a manager to a team
#### Example data
```
[ 3, 7]
```

### DELETE /teams/{team_id}/managers/{user_id}
#### Remove manager from team

### GET /teams/{team_id}/default_emails
#### Get's a team's default emails, the ones created for an event by default
#### Response
```
[
    {
        "id": 677,
        "title": "Can you play?",
        "days_before": 1,
        "content": "Please respond if you can play this week.",
        "event_id": 0,
        "include_rsvp_form": true,
        "send_type": "days_before",
        "send_on": null,
        "team_id": 29,
        "player_types": null,
        "response_types": null,
        "default": true
    },
    {
        "id": 3314,
        "title": "Updated title",
        "days_before": 3,
        "content": "Please RSVP",
        "event_id": 1254,
        "include_rsvp_form": true,
        "send_type": "days_before",
        "send_on": null,
        "team_id": 29,
        "player_types": null,
        "response_types": null,
        "default": true
    },
    {
        "id": 3315,
        "title": "Updated title",
        "days_before": 3,
        "content": "Please RSVP",
        "event_id": 1254,
        "include_rsvp_form": true,
        "send_type": "days_before",
        "send_on": null,
        "team_id": 29,
        "player_types": null,
        "response_types": null,
        "default": true
    }
]
```


### GET /teams/{team_id}/events
#### Get all events for a team
```
[
    {
        "id": 1294,
        "name": "Game 5 vs. Team Ramrod",
        "team_id": 186,/play
        "timezone": "America/Chicago",
        "start": "2013-12-08T10:00:00.000-06:00",
        "end": "2013-12-08T11:00:00.000-06:00",
        "description": "Please RSVP with your status.",
        "location": "Dodge County Ice Arena, Kasson",
        "default_response": {
            "id": 1,
            "label": "No Response"
        }
    },
    {
        "id": 1295,
        "name": "Game 6 vs. Beastiality Boys",
        "team_id": 186,
        "timezone": "America/Chicago",
        "start": "2013-12-08T11:00:00.000-06:00",
        "end": "2013-12-08T12:00:00.000-06:00",
        "description": "Please RSVP with your status.",
        "location": "Dodge County Ice Arena, Kasson",
        "default_response": {
            "id": 1,
            "label": "No Response"
        }
    }
]
```

### GET /teams/{team_id}/past_events
#### Get all events for a team
```
[
    {
        "id": 1290,
        "name": "Game 1 vs. Team Ramrod",
        "team_id": 186,
        "timezone": "America/Chicago",
        "start": "2013-11-17T10:00:00.000-06:00",
        "end": "2013-11-17T11:00:00.000-06:00",
        "description": "Please RSVP with your status.",
        "location": "Dodge County Ice Arena, Kasson",
        "default_response": {
            "id": 1,
            "label": "No Response"
        }
    },
    {
        "id": 1291,
        "name": "Game 2 vs. Beastiality Boys",
        "team_id": 186,
        "timezone": "America/Chicago",
        "start": "2013-11-17T11:00:00.000-06:00",
        "end": "2013-11-17T12:00:00.000-06:00",
        "description": "Please RSVP with your status.",
        "location": "Dodge County Ice Arena, Kasson",
        "default_response": {
            "id": 1,
            "label": "No Response"
        }
    },
    {
        "id": 1292,
        "name": "Game 3 vs. Beastiality Boys",
        "team_id": 186,
        "timezone": "America/Chicago",
        "start": "2013-11-24T10:00:00.000-06:00",
        "end": "2013-11-24T11:00:00.000-06:00",
        "description": "Please RSVP with your status.",
        "location": "Dodge County Ice Arena, Kasson",
        "default_response": {
            "id": 1,
            "label": "No Response"
        }
    },
    {
        "id": 1293,
        "name": "Game 4 vs. Team Ramrod",
        "team_id": 186,
        "timezone": "America/Chicago",
        "start": "2013-12-01T11:00:00.000-06:00",
        "end": "2013-12-01T12:00:00.000-06:00",
        "description": "Please RSVP with your status.",
        "location": "Dodge County Ice Arena, Kasson",
        "default_response": {
            "id": 1,
            "label": "No Response"
        }
    }
]
```

## User Resource

### GET /users
#### Get my info
```
{
    "id": 3,
    "email": "junker37@gmail.com",
    "first_name": "Jeremy",
    "last_name": "McJunkin"
}
```

### PUT /users
#### Update the logged in users personal info
```
{
  "id": 3,
  "first_name": "test",
  "last_name": "asdfadsf",
  "email":"junker37@gmail.com"
}
```

## Player Resource

### GET /players
#### Get's all of the user's players
```
[
    {
        "id": 68,
        "user_id": 3,
        "team_id": 3,
        "user": null,
        "team": {
            "id": 3,
            "owner_id": null,
            "name": "Sloppy Waffles",
            "type": null,
            "default_location": null,
            "description": null
        },
        "player_type": {
            "id": 1,
            "label": "Regular"
        }
    },
    {
        "id": 152,
        "user_id": 3,
        "team_id": 21,
        "user": null,
        "team": {
            "id": 21,
            "owner_id": null,
            "name": "Eagle Drug",
            "type": null,
            "default_location": null,
            "description": null
        },
        "player_type": {
            "id": 2,
            "label": "Sub[
    {
        "id": 68,
        "user_id": 3,
        "team_id": 3,
        "user": null,
        "team": {
            "id": 3,
            "owner_id": null,
            "name": "Sloppy Waffles",
            "type": null,
            "default_location": null,
            "description": null
        },
        "player_type": {
            "id": 1,
            "label": "Regular"
        }
    },
    {
        "id": 152,
        "user_id": 3,
        "team_id": 21,
        "user": null,
        "team": {
            "id": 21,
            "owner_id": null,
            "name": "Eagle Drug",
            "type": null,
            "default_location": null,
            "description": null
        },
        "player_type": {
            "id": 1,
            "label": "Regular"
        }
    }"
        }
    }
]
```

### Get /players/team/{team_id}/me
Gets the player object for the team for the logged in user
```
{
    "id": 1282,
    "user_id": 666,
    "team_id": 29,
    "user": {
        "id": 666,
        "email": "tomcaflisch@gmail.com",
        "first_name": "Tom",
        "last_name": "Caflisch"
    },
    "team": {
        "id": 29,
        "owner_id": null,
        "name": "Friday Broomball",
        "type": null,
        "default_location": null,
        "description": null
    },
    "player_type": "Regular"
}
```

### POST /players
#### Example data
will create a new user if it doesn't exist
```
{
  "team_id": 3,
  "email": "junker37@gmail.com",
  "player_type_id": 2
}
```
#### Response
```
[
    {
        "id": 68,
        "user_id": 3,
        "team_id": 3,
        "user": {
            "id": 3,
            "email": "junker37@gmail.com",
            "first_name": "Jeremy",
            "last_name": "McJunkin"
        },
        "player_type": "Regular",
        "player_type_id": 1
    },
    {
        "id": 51,
        "user_id": 64,
        "team_id": 3,
        "user": {
            "id": 64,
            "email": "boecker@us.ibm.com",
            "first_name": "Doug",
            "last_name": "Boecker"
        },
        "player_type": "Regular",
        "player_type_id": 1
    },
    {
        "id": 1221,
        "user_id": 129,
        "team_id": 3,
        "user": {
            "id": 129,
            "email": "nathan.rabe@gmail.com",
            "first_name": "Nate",
            "last_name": "Rabe"
        },
        "player_type": "Sub",
        "player_type_id": 2
    }
]
```

### DELETE /players/{player_id}
#### Remove player from team

### PUT /players/{player_id}/{player_type_id}
#### Change player type

## Events

### GET /events
#### Get user's next 3 upcoming events
#### Response
```
[
    {
        "id": 1253,
        "name": "Pickup Games",
        "team_id": 29,
        "start": "2013-11-15",
        "end": "2013-11-15",
        "description": "",
        "location": "Graham Arena Rink #4"
    },
    {
        "id": 1254,
        "name": "Pickup Games",
        "team_id": 29,
        "start": "2013-11-22",
        "end": "2013-11-22",
        "description": "",
        "location": "Graham Arena Rink #4"
    },
    {
        "id": 1255,
        "name": "Pickup Games",
        "team_id": 29,
        "start": "2013-11-29",
        "end": "2013-11-29",
        "description": "",
        "location": "Graham Arena Rink #4"
    }
]
```

### GET /events/{event_id}
#### Get an event
#### Response
```
{
    "id": 1254,
    "name": "Pickup Games",
    "team_id": 29,
    "timezone": "America/Chicago",
    "start": "2013-11-22T11:30:00.000-06:00",
    "end": "2013-11-22T13:30:00.000-06:00",
    "description": "",
    "location": "Graham Arena Rink #4",
    "default_response": {
        "id": 1,
        "label": "No Response"
    }
}
```

### POST /events
#### Create an event
#### Example data
```
{
    "name": "Pickup Games",
    "team_id": 29,
    "timezone":"America/Los_Angeles",
    "start": "2013-12-15 10:30:00",
    "end": "2013-12-15 11:45:00",
    "description": "This is a test event",
    "location": "Graham Arena Rink #4"
}
```
#### Response
```
TODO
```

### PUT /events/{event_id}
#### Update an event
#### Example data
```
```

### DELETE /events/{event_id}
#### Delete an event

```
{
    "id": 1253,
    "name": "Pickup Games - update name",
    "team_id": 29,
    "start": "2013-12-15",
    "end": "2013-12-15",
    "description": "",
    "location": "Graham Arena Rink #4"
}
```

### GET /events/{event_id}/responses
#### Gets responses for an event
#### Response
```
[
    {
        "id": 3,
        "event_id": 1254,
        "player_id": 188,
        "player_info": {
            "email": "junker37@gmail.com",
            "firstName": "Jeremy",
            "lastName": "McJunkin",
            "userId": 3,
            "playerType": {
                "id": 1,
                "label": "Regular"
            }
        },
        "response": {
            "id": 2,
            "label": "Yes"
        },
        "created": "2007-11-13T16:34:36.000Z",
        "comment": null,
        "response_type_id": 2
    },
    {
        "id": 11,
        "event_id": 1254,
        "player_id": 220,
        "player_info": {
            "email": "zane131@gmail.com",
            "firstName": "Zane",
            "lastName": "Shelley",
            "userId": 11,
            "playerType": {
                "id": 1,
                "label": "Regular"
            }
        },
        "response": {
            "id": 1,
            "label": "No Response"
        },
        "created": "2007-11-20T09:16:10.000Z",
        "comment": null,
        "response_type_id": 1
    }
]
```

### GET /events/{event_id}/emails
#### Gets emails for an event
#### Response
```
[
    {
        "id": 3216,
        "title": "Can you play?",
        "days_before": 1,
        "content": "Please respond if you can play this week.",
        "event_id": 1254,
        "include_rsvp_form": true,
        "send_type": "days_before",
        "send_on": null,
        "team_id": 0,
        "default": false
    }
]
```

### POST /responses
#### Create a response
#### Example data
```
{
  "response_type_id":2,
  "event_id":1254,
  "player_id":188
}
```

### GET /responses/{event_id}
#### Get logged in user's responses for event, latest one first
#### Response
```
[
    {
        "id": 11873,
        "event_id": 1254,
        "player_id": 188,
        "response": {
            "id": 5,
            "label": "No"
        },
        "created": "2013-11-19T04:04:06.000-06:00",
        "comment": null,
        "response_type_id": 5
    },
    {
        "id": 11872,
        "event_id": 1254,
        "player_id": 188,
        "response": {
            "id": 2,
            "label": "Yes"
        },
        "created": "2013-11-19T03:57:02.000-06:00",
        "comment": null,
        "response_type_id": 2
    }
]
```


## Email Resource

### POST /emails
#### If "default" is true, team_id must be set
#### Example data
possible send types: now, days_before, send_on
if send_type is send_on, send_on must not be null
if send_type is days_before, days_before must be non-negative
a default email can only be of send_type: days_before
```
{
  "title":"Updated title",
  "content":"Please RSVP",
  "days_before":3,
  "event_id":1254,
  "include_rsvp_form":true,
  "send_type":"days_before",
  "player_types": [ 1, 2 ],
  "response_types": [ 2, 5 ],
  "default":false,
  "team_id":null
}
```
#### Response
```
{
    "id": 3313,
    "title": "Updated title",
    "days_before": 3,
    "content": "Please RSVP",
    "event_id": 1254,
    "include_rsvp_form": true,
    "send_type": "days_before",
    "send_on": null,
    "team_id": null,
    "player_types": [
        1,
        2
    ],
    "response_types": [
        2,
        5
    ],
    "default": false
}
```

### GET /emails/{email_id}
#### Response
```
{
    "id": 3313,
    "title": "Updated title",
    "days_before": 3,
    "content": "Please RSVP",
    "event_id": 1254,
    "include_rsvp_form": true,
    "send_type": "days_before",
    "send_on": null,
    "team_id": 0,
    "player_types": [
        1,
        2
    ],
    "response_types": [
        2,
        5
    ],
    "default": false
}
```

### PUT /emails
#### Example data
```
{
    "id": 3313,
    "title": "Updated title",
    "days_before": 3,
    "content": "Please RSVP",
    "event_id": 1254,
    "include_rsvp_form": true,
    "send_type": "days_before",
    "send_on": null,
    "team_id": 0,
    "player_types": [
        3
    ],
    "response_types": [
        1,
        3
    ],
    "default": false
}
```
#### Response
```
{
    "id": 3313,
    "title": "Updated title",
    "days_before": 3,
    "content": "Please RSVP",
    "event_id": 1254,
    "include_rsvp_form": true,
    "send_type": "days_before",
    "send_on": null,
    "team_id": 0,
    "player_types": [
        3
    ],
    "response_types": [
        1,
        3
    ],
    "default": false
}
```

### DELETE /emails/{email_id}
#### Delete email

### PUT /emails/{email_id}/make_default
#### Copies this email and creates one that is a default for the team
#### Response
```
{
    "id": 3315,
    "title": "Updated title",
    "days_before": 3,
    "content": "Please RSVP",
    "event_id": 1254,
    "include_rsvp_form": true,
    "send_type": "days_before",
    "send_on": null,
    "team_id": 29,
    "player_types": [
        3
    ],
    "response_types": [
        3,
        1
    ],
    "default": true
}
```

### POST /emails/{email_id}/send
Send an existing email

