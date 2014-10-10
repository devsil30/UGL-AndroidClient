package edu.purdue.ugl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemBag extends ListFragment {
    Button addButton;
    ListView listview;
    private SimpleAdapter sa;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_item_bag,container,false);
        listview = (ListView) rootView.findViewById(android.R.id.list);
        addButton = (Button) rootView.findViewById(R.id.newItemBag);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(v.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Create a new Item");
                final EditText title = new EditText(v.getContext());
                title.setHint("Title, no more than 32 characters");
                layout.addView(title);

                final EditText description = new EditText(v.getContext());
                description.setHint("Description");
                layout.addView(description);
                alert.setView(layout);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tvalue = title.getText().toString();
                        String dvalue = description.getText().toString();
                        HashMap<String,String> item;
                        item = new HashMap<String, String>();
                        item.put("title",tvalue);
                        item.put("description",dvalue);
                        list.add(item);

                    }
                });
                alert.show();
            }
        });
        sa = new SimpleAdapter(getActivity().getBaseContext(),list,R.layout.listview_layout,
                new String [] {"title","description"}, new int[] {R.id.title, R.id.description});
        listview.setAdapter(sa);
        return rootView;
    }

}
