package com.example.proiect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList;
    private DatabaseHelper databaseHelper;
    private int currentUserId;
    private double currentLatitude;
    private double currentLongitude;

    // Constructor
    public UserAdapter(Context context, List<User> userList, int currentUserId, double currentLatitude, double currentLongitude) {
        this.context = context;
        this.userList = userList;
        this.databaseHelper = new DatabaseHelper(context);
        this.currentUserId = currentUserId;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
    }


    // Metode standard
    @Override
    public int getCount() {
        return userList != null ? userList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return userList != null ? userList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        User user = (User) getItem(position);
        return user != null ? user.getId() : position;
    }

    // Metoda de getView construieste fiecare item
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
            holder = new ViewHolder();
            holder.textViewAge = view.findViewById(R.id.textViewAge);
            holder.cardView = view.findViewById(R.id.card_view_root);
            holder.textViewName = view.findViewById(R.id.textViewName);
            holder.textViewCity = view.findViewById(R.id.textViewCity);
            holder.textViewGender = view.findViewById(R.id.textViewGender);
            holder.textViewBio = view.findViewById(R.id.textViewBio);
            holder.textViewLikesCount = view.findViewById(R.id.textViewLikesCount);
            holder.buttonLocation = view.findViewById(R.id.buttonLocation);
            holder.buttonLike = view.findViewById(R.id.buttonLike);
            holder.textViewDistance = view.findViewById(R.id.textViewDistance);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        User user = (User) getItem(position);

        double distance = calculateDistance(currentLatitude, currentLongitude, user.getLatitude(), user.getLongitude());
        holder.textViewDistance.setText(String.format("Distanta: %.2f km", distance));

        int age = calculateAge(user.getBirthdate());
        holder.textViewAge.setText("Varsta: " + age + " ani");

        if (user != null) {
            holder.textViewName.setText(user.getName() != null ? user.getName() : "Anonim");
            holder.textViewCity.setText(user.getCity() != null ? "Oras: " + user.getCity() : "Oras necunoscut");
            holder.textViewGender.setText(user.getGender() != null ? "Gen: " + user.getGender() : "Gen necunoscut");
            holder.textViewBio.setText(user.getBio() != null ? "Despre: " + user.getBio() : "Fara descriere");
            holder.textViewLikesCount.setText(user.getLikesCount() + " Like-uri");

            // Aici configurez butonul de like si caseta (style diferit daca am dat like sau nu)
            if (!databaseHelper.hasUserAlreadyLiked(currentUserId, user.getId())) {
                holder.buttonLike.setText("Like ❤️");
                holder.cardView.setCardBackgroundColor(android.graphics.Color.WHITE);
            } else {
                holder.buttonLike.setText("Dislike 💔");
                holder.cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#fff8f8"));
            }

            // Aici am logica butonului de harta (imi arata user-ul pe harta efectiv)
            holder.buttonLocation.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(context, ViewUserLocationActivity.class);
                    intent.putExtra("latitude", user.getLatitude());
                    intent.putExtra("longitude", user.getLongitude());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Eroare la harta", Toast.LENGTH_SHORT).show();
                }
            });

            holder.buttonLike.setOnClickListener(v -> {
                try {
                    if (!databaseHelper.hasUserAlreadyLiked(currentUserId, user.getId())) {
                        // Dacă NU a dat like, adaugă like
                        databaseHelper.addLike(currentUserId, user.getId());
                        user.setLikesCount(user.getLikesCount() + 1);
                        Toast.makeText(context, "Ai dat like lui " + user.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Dacă A DAT deja like, atunci șterge like-ul (dislike)
                        databaseHelper.removeLike(currentUserId, user.getId());
                        user.setLikesCount(user.getLikesCount() - 1);
                        Toast.makeText(context, "Ai dat dislike lui " + user.getName(), Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged(); // Refresh lista
                } catch (Exception e) {
                    Toast.makeText(context, "Eroare la like/dislike", Toast.LENGTH_SHORT).show();
                }
            });


        }

        return view;
    }

    // Haversine pentru a calcula distanta dintre puncte (luata de pe net logica :))) )
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raza pamantului in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Calculare varsta in functie de data la care e nascuta persoana si data actuala
    private int calculateAge(String birthdate) {
        try {
            String[] parts = birthdate.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            java.util.Calendar today = java.util.Calendar.getInstance();
            int age = today.get(java.util.Calendar.YEAR) - year;

            if (today.get(java.util.Calendar.MONTH) + 1 < month ||
                    (today.get(java.util.Calendar.MONTH) + 1 == month && today.get(java.util.Calendar.DAY_OF_MONTH) < day)) {
                age--;
            }

            return age;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewCity;
        TextView textViewGender;
        TextView textViewBio;
        TextView textViewLikesCount;
        Button buttonLocation;
        Button buttonLike;
        TextView textViewDistance;
        TextView textViewAge;

        androidx.cardview.widget.CardView cardView;
    }
}
