package ibelieveicanfly.skydivelogbook;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UserProfileActivity extends AppCompatActivity {

    private ImageButton profilePicture;
    private TextView name;
    private TextView age;
    private TextView yis;
    private TextView jumps;
    private TextView certificate;
    private TextView dropzone;

    private ImageView imageEdit;
    private ImageButton cancelEdit;
    private ImageButton confirmEdit;
    private ImageButton edit;
    private EditText ageEdit;
    private EditText yisEdit;
    private EditText jumpsEdit;
    private Spinner certificateEdit;
    private EditText dropzoneEdit;

    private User tempUser;
    private String uid;
    private Uri filePath;
    private boolean changedPic = false;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference storageReference2;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profilePicture = findViewById(R.id.profilePicture);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        yis = findViewById(R.id.yis);
        jumps = findViewById(R.id.jumps);
        certificate = findViewById(R.id.certificate);
        dropzone = findViewById(R.id.dropzone);

        imageEdit = findViewById(R.id.imageEdit);
        cancelEdit = findViewById(R.id.cancelEdit);
        confirmEdit = findViewById(R.id.confirmEdit);
        edit = findViewById(R.id.editButton);
        ageEdit = findViewById(R.id.ageEdit);
        yisEdit = findViewById(R.id.yisEdit);
        jumpsEdit = findViewById(R.id.jumpsEdit);
        certificateEdit = findViewById(R.id.certificateEdit);
        dropzoneEdit = findViewById(R.id.dropzoneEdit);

        imageEdit.setVisibility(View.GONE);
        cancelEdit.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);
        ageEdit.setVisibility(View.GONE);
        yisEdit.setVisibility(View.GONE);
        jumpsEdit.setVisibility(View.GONE);
        certificateEdit.setVisibility(View.GONE);
        dropzoneEdit.setVisibility(View.GONE);

        profilePicture.setClickable(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.certificateEditString, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        certificateEdit.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getFromDB();

    }

    public void setAge(View view){
        DateDialog dialog = new DateDialog(view, tempUser.dateOfBirth);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, "DatePicker");
    }

    private void getFromDB() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference ref = usersRef.child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUser(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to get current user
            }
        });
    }

    private void getUser(User user) {
        tempUser = user;

        updateImage();

        name.setText(user.firstName + " " + user.lastName);
        age.setText(calculateAge(user) + " (" + user.dateOfBirth + ")");
        if (user.yearsInSport != null) yis.setText(user.yearsInSport);
        if (user.totalJumps != null) jumps.setText(user.totalJumps);
        certificate.setText(user.certificate);
        if (user.primaryDropzone != null) dropzone.setText((user.primaryDropzone));

        if (user.userID.equals(auth.getCurrentUser().getUid())) {
            edit.setVisibility(View.VISIBLE);
        } else {
            edit.setVisibility(View.GONE);
        }
    }

    public void editClick(View view) {
        ageEdit.setText(tempUser.dateOfBirth);
        yisEdit.setText(tempUser.yearsInSport);
        jumpsEdit.setText(tempUser.totalJumps);
        if (tempUser.certificate.equals("Learner")) certificateEdit.setSelection(0);
        else if (tempUser.certificate.equals("A")) certificateEdit.setSelection(1);
        else if (tempUser.certificate.equals("B")) certificateEdit.setSelection(2);
        else if (tempUser.certificate.equals("C")) certificateEdit.setSelection(3);
        else if (tempUser.certificate.equals("D")) certificateEdit.setSelection(4);
        dropzoneEdit.setText(tempUser.primaryDropzone);

        cancelEdit.setVisibility(View.VISIBLE);
        confirmEdit.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);

        changeVisibilityAll();
    }

    public void confirmClick(View view) {

        tempUser.dateOfBirth = ageEdit.getText().toString();
        tempUser.yearsInSport = yisEdit.getText().toString();
        tempUser.totalJumps = jumpsEdit.getText().toString();
        tempUser.certificate = certificateEdit.getSelectedItem().toString();
        tempUser.primaryDropzone = dropzoneEdit.getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(uid).setValue(tempUser);

        cancelEdit.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);

        changeVisibilityAll();

        getFromDB();

        changedPic = false;
        Toast.makeText(this, getString(R.string.settingsSaved), Toast.LENGTH_SHORT).show();
    }

    public void cancelClick(View view) {
        cancelEdit.setVisibility(View.GONE);
        confirmEdit.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);

        changeVisibilityAll();
    }

    private void changeVisibility(TextView text, EditText edit) {
        if (text.getVisibility() == View.VISIBLE) {
            text.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            edit.setEnabled(true);
        } else {
            text.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            edit.setEnabled(false);
        }
    }

    private void changeVisibilityAll() {
        if (profilePicture.isClickable()) profilePicture.setClickable(false);
        else profilePicture.setClickable(true);
        if (imageEdit.getVisibility() == View.VISIBLE) imageEdit.setVisibility(View.GONE);
        else imageEdit.setVisibility(View.VISIBLE);
        changeVisibility(age, ageEdit);
        changeVisibility(yis, yisEdit);
        changeVisibility(jumps, jumpsEdit);
        if (certificate.getVisibility() == View.VISIBLE) {
            certificate.setVisibility(View.GONE);
            certificateEdit.setVisibility(View.VISIBLE);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            certificate.setVisibility(View.VISIBLE);
            certificateEdit.setVisibility(View.GONE);
        }
        changeVisibility(dropzone, dropzoneEdit);
    }

    private String calculateAge(User user) {
        String s = "";
        String temp = user.dateOfBirth;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dob = format.parse(temp);
            Date date = new Date();

            long diff = (date.getTime() - dob.getTime());
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long years = days / 365;
            s = String.valueOf(years);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void chooseImage(View view) {
        changedPic = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.uploading));
            progressDialog.show();

            StorageReference ref = storageReference.child("PP/"+ uid);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, getString(R.string.uploaded), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, getString(R.string.uploadFailed), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(getString(R.string.uploadedProgress) + " " + (int)progress + "%");
                }
            });
        }
    }

    private void updateImage() {
        storageReference2 = FirebaseStorage.getInstance().getReference().child("PP").child(uid);
        storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(UserProfileActivity.this)
                        .load(uri)
                        .centerCrop()
                        .into(profilePicture);
            }
        });
    }
}


