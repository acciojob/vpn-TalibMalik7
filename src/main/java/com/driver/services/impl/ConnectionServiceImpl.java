package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
       User user = userRepository2.findById(userId).get();
       if(user.getConnected()){
           throw new Exception("Already connected");
       }
       else if(user.getOriginalCountry().getCountryName().toString().equalsIgnoreCase(countryName)){
           return user;
       }
        if (user.getServiceProviderList() == null) {  // check size or use isempty()
            throw new Exception("Unable to connect");
        }
       else{
           ServiceProvider serviceProvider = null;
           Country country =null;
            int min = Integer.MAX_VALUE;
           List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
           for(ServiceProvider sp : serviceProviderList){
               List<Country> countryList = sp.getCountryList();
               for(Country c : countryList){
                   if(c.getCountryName().toString().equalsIgnoreCase(countryName) && min>sp.getId()){
                       min = sp.getId();
                       serviceProvider  = sp;
                       country =c;

                   }
               }
           }
           if(serviceProvider == null){
               throw new Exception("Unable to connect");
           }
           Connection connection = new Connection();
           connection.setUser(user);
           connection.setServiceProvider(serviceProvider);
           user.setConnected(true);
           user.setMaskedIp(country.getCode()+"."+serviceProvider.getId()+"."+userId);
           user.getConnectionList().add(connection);

           serviceProvider.getConnectionList().add(connection);
           userRepository2.save(user);
           serviceProviderRepository2.save(serviceProvider);
       }
       return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {
       User user = userRepository2.findById(userId).get();
       if(!user.getConnected()){
           throw new Exception("Already disconnected");
       }
       user.setConnected(false);
       user.setMaskedIp(null);
       userRepository2.save(user);
       return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender = userRepository2.findById(senderId).get();
        User reciver = userRepository2.findById(receiverId).get();

        CountryName reciverCountryName = null;
        if(reciver.getConnected()){
            String reciverCountryCode;
            String[] arr = reciver.getMaskedIp().split("\\.");
            reciverCountryCode = arr[0];
            for(CountryName countryName : CountryName.values()){
                if(countryName.toCode().equals(reciverCountryCode)){
                    reciverCountryName = countryName;
                    break;
                }
            }
        }else{
            reciverCountryName = reciver.getOriginalCountry().getCountryName();
        }

        if(reciverCountryName.equals(sender.getOriginalCountry().getCountryName())){
            return sender;
        }

        try {
            sender = connect(senderId, reciverCountryName.name());
        }catch (Exception e){
            throw new Exception("Cannot establish communication");
        }

        return sender;

    }
}
