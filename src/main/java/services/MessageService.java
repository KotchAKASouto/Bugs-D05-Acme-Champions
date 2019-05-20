
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.Authority;
import domain.Actor;
import domain.Message;
import forms.MessageForm;

@Service
@Transactional
public class MessageService {

	//Managed Repository

	@Autowired
	private MessageRepository		messageRepository;

	//Supporting services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;

	@Autowired
	private FinderService			finderService;


	//Simple CRUD methods

	public MessageForm create(final int recipientId) {

		Assert.notNull(recipientId);
		Assert.isTrue(recipientId != 0);

		final MessageForm result = new MessageForm();
		Actor recipient;
		Actor sender;

		recipient = this.actorService.findOne(recipientId);
		Assert.notNull(recipient);

		result.setRecipientId(recipient.getId());

		sender = this.actorService.findByPrincipal();
		result.setSenderId(sender.getId());

		return result;

	}

	public MessageForm create2() {

		final MessageForm result = new MessageForm();

		final Actor actor = this.actorService.findByPrincipal();

		result.setSenderId(actor.getId());

		result.setTags("SYSTEM");

		return result;

	}

	public Message create3() {

		final Message result = new Message();

		Date momentSent;

		final Actor actor = this.actorService.findByPrincipal();

		momentSent = new Date(System.currentTimeMillis() - 1000);

		final Boolean spam = false;

		result.setSender(actor);
		result.setMoment(momentSent);
		result.setSpam(spam);

		return result;

	}

	public Collection<Message> findAll() {

		final Collection<Message> messages = this.messageRepository.findAll();

		Assert.notNull(messages);

		return messages;
	}

	public Message findOne(final int messageId) {

		final Message message = this.messageRepository.findOne(messageId);

		Assert.notNull(message);

		return message;
	}

	public Message save(final Message message) {

		if (message.getId() != 0)
			Assert.isTrue((message.getSender() == this.actorService.findByPrincipal()) || message.getRecipient() == this.actorService.findByPrincipal());

		Assert.notNull(message);

		Message result;

		final Boolean spam1 = this.configurationService.spamContent(message.getSubject());

		final Boolean spam2 = this.configurationService.spamContent(message.getBody());

		final Boolean spam3 = this.configurationService.spamContent(message.getTags());

		if (spam1 || spam2 || spam3)
			message.setSpam(true);

		if (message.getTags().contains("SYSTEM")) {

			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor);
			final Authority authority = new Authority();
			authority.setAuthority(Authority.ADMIN);
			if (actor.getUserAccount().getAuthorities().contains(authority)) {
				if (message.getRecipient() != null)
					message.setTags(null);
			} else
				message.setTags(null);
		}

		if (message.getTags().contains("NOTIFICATION"))
			message.setTags(null);

		result = this.messageRepository.save(message);

		return result;
	}

	public Message save2(final Message message) {

		Assert.notNull(message);

		Message result;

		result = this.messageRepository.save(message);

		return result;

	}

	public void delete(final Message message) {

		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final String tags = message.getTags();

		if (tags.contains("DELETE"))
			this.messageRepository.delete(message);
		else {
			message.setTags("DELETE");
			this.messageRepository.save(message);

		}
	}

	public void deleteAll(final int actorId) {

		final Collection<Message> messages = this.messageRepository.AllmessagePerActor(actorId);

		if (!messages.isEmpty())
			for (final Message m : messages)
				this.messageRepository.delete(m);

	}

	public Boolean securityMessage(final int messageId) {

		Boolean res = false;

		final Message m = this.messageRepository.findOne(messageId);

		final Actor login = this.actorService.findByPrincipal();

		final Actor senderMessage = m.getSender();

		if (m.getTags().equals("SYSTEM")) {

			if ((login.equals(senderMessage)))
				res = true;

		} else {
			final Actor recipientMessage = m.getRecipient();

			if ((login.equals(senderMessage)) || (login.equals(recipientMessage)))
				res = true;

		}

		return res;
	}

	public Boolean securityDisplayMessage(final int messageId) {

		Boolean res = false;

		final Actor senderMessage = this.messageRepository.findOne(messageId).getSender();

		final Actor recipientMessage = this.messageRepository.findOne(messageId).getRecipient();

		final Message m = this.messageRepository.findOne(messageId);

		final Actor login = this.actorService.findByPrincipal();

		if (m.getTags() != null)
			if (m.getTags().equals("SYSTEM"))
				res = true;

		if ((login.equals(senderMessage)) || (login.equals(recipientMessage)))
			res = true;

		return res;
	}

	public Collection<Message> messagePerActor(final int actorId) {

		final Collection<Message> messages = this.messageRepository.messagePerActor(actorId);

		return messages;
	}

	public boolean existId(final int messageId) {
		Boolean res = false;

		final Message message = this.messageRepository.findOne(messageId);

		if (message != null)
			res = true;

		return res;
	}

	public Message reconstruct(final MessageForm message, final BindingResult binding) {

		final Message result = new Message();

		result.setId(message.getId());
		result.setVersion(message.getVersion());
		result.setBody(message.getBody());
		if (message.getRecipientId() != 0)
			result.setRecipient(this.actorService.findOne(message.getRecipientId()));
		result.setSender(this.actorService.findOne(message.getSenderId()));
		result.setSpam(false);
		result.setSubject(message.getSubject());
		result.setTags(message.getTags());

		if (message.getId() == 0) {

			Date momentSent;
			momentSent = new Date(System.currentTimeMillis() - 1000);
			result.setMoment(momentSent);

		} else
			result.setMoment(this.messageRepository.findOne(message.getId()).getMoment());

		final Date momentSent = new Date(System.currentTimeMillis() - 1000);
		result.setMoment(momentSent);

		this.validator.validate(result, binding);

		return result;
	}

	public Collection<Message> AllmessagePerActor(final int actorId) {

		final Collection<Message> result = this.messageRepository.AllmessagePerActor(actorId);

		return result;
	}

	public void flush() {
		this.messageRepository.flush();
	}

}
