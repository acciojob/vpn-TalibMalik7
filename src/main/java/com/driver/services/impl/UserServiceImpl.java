package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{

        Country country = new Country();

        CountryName cname;
        String code;
        if(countryName.equalsIgnoreCase("IND")){   //either use ignorecase which just ignore upper or lower case
            cname = CountryName.IND;                      // or do the classic thing ie make incoming parameter to uppercase using String.toUpperCase method
            code = CountryName.IND.toCode();

        }
        else if(countryName.equalsIgnoreCase("USA")){
            cname = CountryName.USA;
            code = CountryName.USA.toCode();
        }
        else if(countryName.equalsIgnoreCase("AUS")){
            cname = CountryName.AUS;
            code = CountryName.AUS.toCode();
        }
        else if(countryName.equalsIgnoreCase("CHI")){
            cname = CountryName.CHI;
            code = CountryName.CHI.toCode();
        }
        else if(countryName.equalsIgnoreCase("JPN")){
            cname = CountryName.JPN;
            code = CountryName.JPN.toCode();
        }
        else{
            throw new Exception("Country not found");
        }
        country.setCountryName(cname);
        country.setCode(code);
        User user = new User(username,password);
        user.setOriginalCountry(country);
        user.setConnected(false);
        user.setMaskedIp(null);
        country.setUser(user);
        userRepository3.save(user);
        String id = String.valueOf(user.getId());
        user.setOriginalIp(code+"."+id);
        userRepository3.save(user);
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User user = userRepository3.findById(userId).get();
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
         serviceProvider.getUsers().add(user);
         user.getServiceProviderList().add(serviceProvider);
         serviceProviderRepository3.save(serviceProvider);
         return user;
    }
}




//import com.driver.repository.ServiceProviderRepository;
//import com.driver.repository.UserRepository;
//import com.driver.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    UserRepository userRepository3;
//    @Autowired
//    ServiceProviderRepository serviceProviderRepository3;
//    @Autowired
//    CountryRepository countryRepository3;
//
//    @Override
//    public User register(String username, String password, String countryName) throws Exception{
//        User user = new User(username,password);
//        Country country = new Country();
//
//        if(!isValidCountryName(countryName)){
//            throw new Exception("Country not found");
//        }
//
//        if (countryName.equalsIgnoreCase("IND")){
//            country.setCountryName(CountryName.IND);
//            country.setCode(CountryName.IND.toCode());
//        }
//        if (countryName.equalsIgnoreCase("USA")){
//            country.setCountryName(CountryName.USA);
//            country.setCode(CountryName.USA.toCode());
//        }
//        if (countryName.equalsIgnoreCase("AUS")){
//            country.setCountryName(CountryName.AUS);
//            country.setCode(CountryName.AUS.toCode());
//        }
//        if (countryName.equalsIgnoreCase("JPN")){
//            country.setCountryName(CountryName.JPN);
//            country.setCode(CountryName.JPN.toCode());
//        }
//        if (countryName.equalsIgnoreCase("CHI")){
//            country.setCountryName(CountryName.CHI);
//            country.setCode(CountryName.CHI.toCode());
//        }
//
//        country.setUser(user);
//        user.setOriginalCountry(country);
//        user.setConnected(false);
//        userRepository3.save(user);
//
//        String IP = country.getCode()+"."+user.getId();
//        user.setOriginalIp(IP);
//
//        userRepository3.save(user);
//        return user;
//    }
//
//    private boolean isValidCountryName(String countryName){
//        if(countryName.equalsIgnoreCase("IND") || countryName.equalsIgnoreCase("USA") || countryName.equalsIgnoreCase("AUS")||countryName.equalsIgnoreCase("JPN")||countryName.equalsIgnoreCase("CHI")) {
//            return true;
//        }
//        else
//            return false;
//    }
//
//    @Override
//    public User subscribe(Integer userId, Integer serviceProviderId) {
//        User user = userRepository3.findById(userId).get();
//        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
//
//        user.getServiceProviderList().add(serviceProvider);
//        serviceProvider.getUsers().add(user);
//
//        serviceProviderRepository3.save(serviceProvider);
//        return user;
//    }
//}
