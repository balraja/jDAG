// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/org/jdryad/com/messages/messages.proto

package org.jdryad.com.messages;

public final class MessageProtos {
  private MessageProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public static final class PingMessage extends
      com.google.protobuf.GeneratedMessage {
    // Use PingMessage.newBuilder() to construct.
    private PingMessage() {
      initFields();
    }
    private PingMessage(boolean noInit) {}
    
    private static final PingMessage defaultInstance;
    public static PingMessage getDefaultInstance() {
      return defaultInstance;
    }
    
    public PingMessage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.jdryad.com.messages.MessageProtos.internal_static_org_jdryad_com_messages_PingMessage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.jdryad.com.messages.MessageProtos.internal_static_org_jdryad_com_messages_PingMessage_fieldAccessorTable;
    }
    
    // required string name = 1;
    public static final int NAME_FIELD_NUMBER = 1;
    private boolean hasName;
    private java.lang.String name_ = "";
    public boolean hasName() { return hasName; }
    public java.lang.String getName() { return name_; }
    
    // required int32 messageType = 2;
    public static final int MESSAGETYPE_FIELD_NUMBER = 2;
    private boolean hasMessageType;
    private int messageType_ = 0;
    public boolean hasMessageType() { return hasMessageType; }
    public int getMessageType() { return messageType_; }
    
    private void initFields() {
    }
    public final boolean isInitialized() {
      if (!hasName) return false;
      if (!hasMessageType) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (hasName()) {
        output.writeString(1, getName());
      }
      if (hasMessageType()) {
        output.writeInt32(2, getMessageType());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(1, getName());
      }
      if (hasMessageType()) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, getMessageType());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static org.jdryad.com.messages.MessageProtos.PingMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(org.jdryad.com.messages.MessageProtos.PingMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> {
      private org.jdryad.com.messages.MessageProtos.PingMessage result;
      
      // Construct using org.jdryad.com.messages.MessageProtos.PingMessage.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new org.jdryad.com.messages.MessageProtos.PingMessage();
        return builder;
      }
      
      protected org.jdryad.com.messages.MessageProtos.PingMessage internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new org.jdryad.com.messages.MessageProtos.PingMessage();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.jdryad.com.messages.MessageProtos.PingMessage.getDescriptor();
      }
      
      public org.jdryad.com.messages.MessageProtos.PingMessage getDefaultInstanceForType() {
        return org.jdryad.com.messages.MessageProtos.PingMessage.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public org.jdryad.com.messages.MessageProtos.PingMessage build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private org.jdryad.com.messages.MessageProtos.PingMessage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public org.jdryad.com.messages.MessageProtos.PingMessage buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        org.jdryad.com.messages.MessageProtos.PingMessage returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.jdryad.com.messages.MessageProtos.PingMessage) {
          return mergeFrom((org.jdryad.com.messages.MessageProtos.PingMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(org.jdryad.com.messages.MessageProtos.PingMessage other) {
        if (other == org.jdryad.com.messages.MessageProtos.PingMessage.getDefaultInstance()) return this;
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasMessageType()) {
          setMessageType(other.getMessageType());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                return this;
              }
              break;
            }
            case 10: {
              setName(input.readString());
              break;
            }
            case 16: {
              setMessageType(input.readInt32());
              break;
            }
          }
        }
      }
      
      
      // required string name = 1;
      public boolean hasName() {
        return result.hasName();
      }
      public java.lang.String getName() {
        return result.getName();
      }
      public Builder setName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasName = true;
        result.name_ = value;
        return this;
      }
      public Builder clearName() {
        result.hasName = false;
        result.name_ = getDefaultInstance().getName();
        return this;
      }
      
      // required int32 messageType = 2;
      public boolean hasMessageType() {
        return result.hasMessageType();
      }
      public int getMessageType() {
        return result.getMessageType();
      }
      public Builder setMessageType(int value) {
        result.hasMessageType = true;
        result.messageType_ = value;
        return this;
      }
      public Builder clearMessageType() {
        result.hasMessageType = false;
        result.messageType_ = 0;
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:org.jdryad.com.messages.PingMessage)
    }
    
    static {
      defaultInstance = new PingMessage(true);
      org.jdryad.com.messages.MessageProtos.internalForceInit();
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:org.jdryad.com.messages.PingMessage)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_org_jdryad_com_messages_PingMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_org_jdryad_com_messages_PingMessage_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n*src/org/jdryad/com/messages/messages.p" +
      "roto\022\027org.jdryad.com.messages\"0\n\013PingMes" +
      "sage\022\014\n\004name\030\001 \002(\t\022\023\n\013messageType\030\002 \002(\005B" +
      "\017B\rMessageProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_org_jdryad_com_messages_PingMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_org_jdryad_com_messages_PingMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_org_jdryad_com_messages_PingMessage_descriptor,
              new java.lang.String[] { "Name", "MessageType", },
              org.jdryad.com.messages.MessageProtos.PingMessage.class,
              org.jdryad.com.messages.MessageProtos.PingMessage.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  public static void internalForceInit() {}
  
  // @@protoc_insertion_point(outer_class_scope)
}
