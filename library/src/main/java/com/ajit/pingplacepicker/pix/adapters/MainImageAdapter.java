package com.ajit.pingplacepicker.pix.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajit.pingplacepicker.pix.interfaces.OnSelectionListener;
import com.ajit.pingplacepicker.pix.interfaces.SectionIndexer;
import com.ajit.pingplacepicker.pix.modals.Img;
import com.ajit.pingplacepicker.pix.utility.HeaderItemDecoration;
import com.ajit.pingplacepicker.pix.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ajit.pingplacepicker.R;

import java.io.File;
import java.util.ArrayList;

public class MainImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderItemDecoration.StickyHeaderInterface, SectionIndexer {

    public static final int HEADER = 1;
    public static final int ITEM = 2;
    public static int SPAN_COUNT = 0;
    private static final int MARGIN = 4;

    private ArrayList<Img> list;
    private OnSelectionListener onSelectionListener;
    private FrameLayout.LayoutParams layoutParams;
    private RequestManager glide;
    private RequestOptions options;

    public MainImageAdapter(Context context, int spanCount) {
        this.list = new ArrayList<>();
        SPAN_COUNT = spanCount;
        int size = (Utility.WIDTH / SPAN_COUNT) - (MARGIN / 2);
        layoutParams = new FrameLayout.LayoutParams(size, size);
        layoutParams.setMargins(MARGIN, MARGIN - (MARGIN / 2), MARGIN, MARGIN - (MARGIN / 2));
        options = new RequestOptions().override(size - 50)
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        glide = Glide.with(context);
    }

    public ArrayList<Img> getItemList() {
        return list;
    }

    public MainImageAdapter addImage(Img image) {
        list.add(image);
        notifyDataSetChanged();
        return this;
    }

    public void addOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    public void addImageList(ArrayList<Img> images) {
        list.addAll(images);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() <= position) {
            return 0;
        }
        Img i = list.get(position);
        return (i.getContentUrl().equalsIgnoreCase("")) ?
                HEADER : ITEM;
    }

    public void clearList() {
        list.clear();
    }

    public void select(boolean selection, int pos) {
        list.get(pos).setSelected(selection);
        notifyItemChanged(pos);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getContentUrl().hashCode();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new HeaderHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.header_row, parent, false));
        } else {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.main_image, parent, false);
            return new Holder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Img image = list.get(position);
        if (holder instanceof Holder) {
            Holder imageHolder = (Holder) holder;
            if (image.getMediaType() == 1) {
                glide.load(image.getContentUrl()).apply(options).into(imageHolder.preview);
                imageHolder.isVideo.setVisibility(View.GONE);
            } else if (image.getMediaType() == 3) {
                glide.asBitmap()
                        .load(Uri.fromFile(new File(image.getUrl())))
                        .apply(options)
                        .into(imageHolder.preview);
                imageHolder.isVideo.setVisibility(View.VISIBLE);
            }
            imageHolder.selection.setVisibility(image.getSelected() ? View.VISIBLE : View.GONE);
        } else if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.header.setText(image.getHeaderDate());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getHeaderPositionForItem(int position) {
        int itemPosition = position;
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.header_row;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        Img image = list.get(headerPosition);
        ((TextView) header.findViewById(R.id.header)).setText(image.getHeaderDate());
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return getItemViewType(itemPosition) == 1;
    }

    @Override
    public String getSectionText(int position) {
        return list.get(position).getHeaderDate();
    }

    public String getSectionMonthYearText(int position) {
        if (list.size() <= position) {
            return "";
        }
        return list.get(position).getHeaderDate();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView preview;
        private ImageView selection;
        private ImageView isVideo;

        Holder(View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            selection = itemView.findViewById(R.id.selection);
            isVideo = itemView.findViewById(R.id.isVideo);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            preview.setLayoutParams(layoutParams);
        }

        @Override
        public void onClick(View view) {
            int id = this.getLayoutPosition();
            onSelectionListener.onClick(list.get(id), view, id);
        }

        @Override
        public boolean onLongClick(View view) {
            int id = this.getLayoutPosition();
            onSelectionListener.onLongClick(list.get(id), view, id);
            return true;
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView header;

        HeaderHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
        }
    }
}
