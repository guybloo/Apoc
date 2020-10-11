package com.example.apoc.DB;

import androidx.annotation.NonNull;

import com.example.apoc.Enums.Fears;
import com.example.apoc.types.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.apoc.types.HelpMethods.ListFromGson;
import static com.example.apoc.types.HelpMethods.ListToGson;

public class GroupsDB extends DBWrapper {
    protected static String NAME = "name";
    protected static String LEADER = "leader";
    protected static String GROUPIES = "groupies";
    protected static String FEARS = "fears";

    public GroupsDB(){
        super();
        docName = "groups";
    }

    @Override
    public void updateItem(DBItem updateItem) {
        Group item = (Group)updateItem;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(NAME, item.getGroupName());
        newItem.put(LEADER, item.getLeader());
        newItem.put(GROUPIES, item.getGroupies());
        newItem.put(FEARS, ListToGson(item.getFears()));

        db.collection(docName).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        return new Group((String) item.get(NAME),
                (String) item.get(LEADER),
//                ListFromGson((String) item.get(GROUPIES),String.class),
                new ArrayList<String>(((ArrayList)item.get(GROUPIES))),
                ListFromGson((String) item.get(FEARS), Fears.class));
    }

    public void getGroupByUser(final String userId){
        items.clear();
        db.collection(docName).whereArrayContains(GroupsDB.GROUPIES, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                Group tempGroup = (Group) parseItem(item);
                                items.put(userId,tempGroup);
                            }

                            notifyGetSpecific();
                        }
                    }
                });
    }
}
