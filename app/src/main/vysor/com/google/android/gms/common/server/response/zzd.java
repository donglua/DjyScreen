// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import java.util.ArrayList;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.zzb;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class zzd implements Parcelable$Creator<FieldMappingDictionary.Entry>
{
    static void zza(final FieldMappingDictionary.Entry entry, final Parcel parcel, final int n) {
        final int zzcr = zzb.zzcr(parcel);
        zzb.zzc(parcel, 1, entry.versionCode);
        zzb.zza(parcel, 2, entry.className, false);
        zzb.zzc(parcel, 3, entry.DG, false);
        zzb.zzaj(parcel, zzcr);
    }
    
    public FieldMappingDictionary.Entry zzcz(final Parcel parcel) {
        ArrayList<FieldMappingDictionary.FieldMapPair> zzc = null;
        final int zzcq = zza.zzcq(parcel);
        int zzg = 0;
        String zzq = null;
        while (parcel.dataPosition() < zzcq) {
            final int zzcp = zza.zzcp(parcel);
            switch (zza.zzgv(zzcp)) {
                default: {
                    zza.zzb(parcel, zzcp);
                    continue;
                }
                case 1: {
                    zzg = zza.zzg(parcel, zzcp);
                    continue;
                }
                case 2: {
                    zzq = zza.zzq(parcel, zzcp);
                    continue;
                }
                case 3: {
                    zzc = zza.zzc(parcel, zzcp, (android.os.Parcelable$Creator<FieldMappingDictionary.FieldMapPair>)FieldMappingDictionary.FieldMapPair.CREATOR);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != zzcq) {
            throw new zza.zza(new StringBuilder(37).append("Overread allowed size end=").append(zzcq).toString(), parcel);
        }
        return new FieldMappingDictionary.Entry(zzg, zzq, zzc);
    }
    
    public FieldMappingDictionary.Entry[] zzhe(final int n) {
        return new FieldMappingDictionary.Entry[n];
    }
}
