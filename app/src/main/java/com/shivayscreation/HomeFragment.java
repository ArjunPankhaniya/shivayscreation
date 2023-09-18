package com.shivayscreation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    String[] idArray = {"1","2","3","4"};
    String[] nameArray = {"Kurti1","Kurti2","Kurti3","Kurti4"};
    int[] imageArray = {R.drawable.kurti1,R.drawable.kurti2,R.drawable.kurti3,R.drawable.kurti4};

    String[] priceArray = {"4000","2000","150","50"};

    String[] descArray = {
            "Are you looking for a Unique Pattern in Your Wardrobe and Highly Stylized Ethnic Designer Kurta Set, then GULMOHAR JAIPUR has the Answer for You. This Apparel is Very Stylish and Comfortable With Beautiful Designs and Patterns.\n" +
                    "Rayon\n" +
                    "Chiffon\n" +
                    "Kurta, Bottomwear & Dupatta\n" +
                    "Rayon\n" +
                    "Machine wash",
            "Floral Printed Rayon A-Line Kurta with Wooden Button Detailing\n" +
                    "We recommend you buy a size larger\n" +
                    "Package contains : 1 kurta\n" +
                    "Machine wash cold\n" +
                    "100% rayon\n" +
                    "No Darts",
            "Color may slightly vary due to photography\n" +
                    "? Straight Fit Kurta ? Kalamkari Printed Yoke & Border ? Highlighted with Kantha Embroidery ? Has a Lining ? Comes with Kalamkari Printed Dupatta ? Enhanced With Kantha Hand Embroidery\n" +
                    "95 in\n" +
                    "1 Kurta & 1 Dupatta\n" +
                    "Kurta & Dupatta\n" +
                    "Chanderi Silk\n" +
                    "Hand wash cold",
            "Are you looking for a Unique Pattern in Your Wardrobe and Highly Stylized Ethnic Designer Dress, then GULMOHAR JAIPUR has the Answer for You. This Apparel is Very Stylish and Comfortable With Beautiful Designs and Patterns.\n" +
                    "Assurance & Authenticity: Quality Satisfaction And Timely Delivery Are Assured. To Be Sure Of Authenticity, We Recommend You To Buy This Product From GULMOHAR JAIPUR Brand Only.\n" +
                    "Cotton\n" +
                    "Machine wash\n" +
                    "No Darts",
    };

    RecyclerView categoryRecyclerview;
    String[] categoryNameArray = {"Kurti","Saree","Lehenga","tshirt"};
    int[] categoryImageArray = {R.drawable.kurti,R.drawable.saree,R.drawable.lehenga,R.drawable.tshirt};

    ArrayList<CategoryList> arrayList;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.home_recyclerview);

        //Display Data In List
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Display Data In Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //Display Data In Horizontal Scroll
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ProductAdapter adapter = new ProductAdapter(getActivity(),nameArray,imageArray,priceArray,descArray,idArray);
        recyclerView.setAdapter(adapter);

        categoryRecyclerview = view.findViewById(R.id.home_recyclerview_category);
        categoryRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        categoryRecyclerview.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for(int i=0;i<categoryNameArray.length;i++){
            CategoryList list = new CategoryList();
            list.setName(categoryNameArray[i]);
            list.setImage(categoryImageArray[i]);
            arrayList.add(list);
        }
        CategoryAdapter catAdapter = new CategoryAdapter(getActivity(),arrayList);
        categoryRecyclerview.setAdapter(catAdapter);

        return view;
    }
}