package eatyourbeets.misc.CardMods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class AfterLifeMod extends AbstractCardModifier
{

    public static final String ID = GR.Animator.CreateID("Afterlife");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(GR.Animator.CreateID("CardMods"));
    public static final String[] TEXT = uiStrings.TEXT;

    public AbstractCardModifier makeCopy()
    {
        return new AfterLifeMod();
    }

    @Override
    public String identifier(AbstractCard card)
    {
        return ID;
    }

    @Override
    public void onExhausted(AbstractCard card) {
        CombatStats.ControlPile.Add(card)
                .OnUpdate(c ->
                {
                    if (!AbstractDungeon.player.exhaustPile.contains(c.card))
                    {
                        c.Delete();
                    }
                })
                .OnSelect(c ->
                {
                    AbstractCard cardToPurge = getRandomCardToPurge();
                    if (cardToPurge == null) {
                        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[2], true));
                    } else if (c.card.hasEnoughEnergy() && c.card.cardPlayable(null)) {
                        //Cards played from Afterlife do not exhaust
                        boolean wasExhaust = false;
                        if (c.card.exhaust) {
                            System.out.println("turning off exhaust");
                            wasExhaust = true;
                            c.card.exhaust = false;
                        }
                        if (c.card.target == AbstractCard.CardTarget.ENEMY || c.card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                            GameActions.Bottom.SelectCreature(c.card).AddCallback(creature ->
                            {
                                if (creature instanceof AbstractMonster) {
                                    AbstractMonster monster = (AbstractMonster)creature;
                                    GameActions.Bottom.PlayCard(c.card, monster).SpendEnergy(true);
                                }
                                AbstractDungeon.player.exhaustPile.removeCard(c.card);
                                AbstractDungeon.player.exhaustPile.removeCard(cardToPurge);
                                c.Delete();
                            });
                        } else {
                            GameActions.Bottom.PlayCard(c.card, null).SpendEnergy(true);
                            AbstractDungeon.player.exhaustPile.removeCard(c.card);
                            AbstractDungeon.player.exhaustPile.removeCard(cardToPurge);
                            c.Delete();
                        }
                        if (wasExhaust) {
                            System.out.println("turning on exhaust");
                            //Put this in action to make sure it runs after card is played
                            GameActions.Bottom.Add(new AbstractGameAction() {
                                @Override
                                public void update() {
                                    c.card.exhaust = true;
                                    isDone = true;
                                }
                            });
                        }
                    }

                });
    }

    private AbstractCard getRandomCardToPurge() {
        ArrayList<AbstractCard> validCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (!CardModifierManager.hasModifier(card, AfterLifeMod.ID)) {
                validCards.add(card);
            }
        }
        if (validCards.isEmpty()) {
            return null;
        }
        return validCards.get(AbstractDungeon.cardRandomRng.random(validCards.size() - 1));
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card)
    {
        return ((card instanceof AnimatorCard) ? TEXT[0] : TEXT[1]) + rawDescription;
    }
}
