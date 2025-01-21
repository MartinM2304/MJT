package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ClientSendException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.JSONParsingException;

import java.util.List;

public interface Client<T> {

    List<T>loadPages(int numPages)throws ClientSendException, JSONParsingException;

    List<T>loadAll()throws ClientSendException, JSONParsingException;
}
