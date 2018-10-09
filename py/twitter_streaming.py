import sys
import socket
import json
import tweepy
import io
from tweepy import OAuthHandler
from tweepy import Stream
from tweepy.streaming import StreamListener

class TweetsListener(StreamListener):

    def __init__(self, socket):

        print("Tweets listener initialized")
        self.client_socket = socket

    def on_data(self, data):

        try:
            jsonMessage = json.loads(data)
            message = (jsonMessage["text"] + "||" + jsonMessage["created_at"]).encode("utf-8")
            print (message)
            self.client_socket.send(message)

        except BaseException as e:
            print ("Error on_data: %s" % str(e))

        return True

    def on_error(self, status):

        print (status)
        return True

def connect_to_twitter(connection, tracks):

    keys = get_twitter_conf()

    api_key = keys[0]
    api_secret =  keys[1]
    access_token =  keys[2]
    access_token_secret =  keys[3]

    auth = OAuthHandler(api_key, api_secret)
    auth.set_access_token(access_token, access_token_secret)

    twitter_stream = Stream(auth, TweetsListener(connection))
    twitter_stream.filter(track=tracks, languages=["pt"])

def get_twitter_conf():
    file = open('twitter.conf')
    lines = file.readlines()
    keys = list(map(lambda x: x.split('=')[1].replace('\n',''),lines))
    file.close()
    return keys
    
    
    

if __name__ == "__main__":

    if len(sys.argv) < 4:
        print("Usage: twitter_streaming.py <hostname> <port> <tracks>", file=sys.stderr)
        exit(-1)

    host = sys.argv[1]
    port = int(sys.argv[2])
    tracks = sys.argv[3:]

    s = socket.socket()
    s.bind((host, port))

    print("Listening on port: %s" % str(port))

    s.listen(5)

    connection, client_access = s.accept()

    print("Recived request from: " + str(client_access))
    print("Initializing listener for theses tracks: ", tracks)

    connect_to_twitter(connection, tracks)






