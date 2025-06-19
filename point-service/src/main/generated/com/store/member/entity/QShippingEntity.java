package com.store.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import jakarta.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShippingEntity is a Querydsl query type for ShippingEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShippingEntity extends EntityPathBase<ShippingEntity> {

    private static final long serialVersionUID = -61669918L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShippingEntity shippingEntity = new QShippingEntity("shippingEntity");

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final DateTimePath<java.util.Date> created = createDateTime("created", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mainYn = createString("mainYn");

    public final QMemberEntity member;

    public final DateTimePath<java.util.Date> modified = createDateTime("modified", java.util.Date.class);

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath subject = createString("subject");

    public final StringPath zipcode = createString("zipcode");

    public QShippingEntity(String variable) {
        this(ShippingEntity.class, forVariable(variable), INITS);
    }

    public QShippingEntity(Path<? extends ShippingEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShippingEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShippingEntity(PathMetadata metadata, PathInits inits) {
        this(ShippingEntity.class, metadata, inits);
    }

    public QShippingEntity(Class<? extends ShippingEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMemberEntity(forProperty("member")) : null;
    }

}

