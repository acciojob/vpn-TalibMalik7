package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin(username, password);
        return  adminRepository1.save(admin);
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
         Admin admin = adminRepository1.findById(adminId).get();
         ServiceProvider serviceProvider = new ServiceProvider(providerName);
         serviceProvider.setAdmin(admin);
         admin.getServiceProviders().add(serviceProvider);
         adminRepository1.save(admin);
         return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
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
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);
        serviceProviderRepository1.save(serviceProvider);
        return serviceProvider;
    }
}
